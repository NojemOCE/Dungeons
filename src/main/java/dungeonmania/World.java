package dungeonmania;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.goal.*;
import dungeonmania.inventory.Inventory;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.response.models.*;
import dungeonmania.staticEntity.*;
import dungeonmania.util.*;
import dungeonmania.gamemode.*;
import dungeonmania.movingEntity.*;
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.factory.*;
import dungeonmania.util.Position;

public class World {

    private Inventory inventory;
    private Gamemode gamemode;
    private Player player;
    private String id;
    private GoalComponent goals;
    private Map<String, CollectableEntity> collectableEntities = new HashMap<>();
    private Map<String, StaticEntity> staticEntities = new HashMap<>();
    private Map<String, MovingEntity> movingEntities = new HashMap<>();
    private int entityCount;
    private Battle currentBattle;
    private String dungeonName;
    private Factory factory;
    private int randomSeed;

    static final double MERCENARY_ARMOUR_DROP = 0.4;
    static final double ZOMBIE_ARMOUR_DROP = 0.2;
    static final double ONE_RING_DROP = 0.1;
    static final int DEFAULT_WIDTH = 50;
    static final int DEFAULT_HEIGHT = 50;

    private int tickCount = 1;
    

    /**
     * Constructor for world that takes the string of the dungeon name to build
     * and a string for the gamemode (Standard, Peaceful, Hard)
     * @param dungeonName Name of dungeon
     * @param gameMode gamemode, e.g. standard
     * @param randomSeed seed for game
     */
    public World(String dungeonName, String gameMode, int randomSeed) {
        this.dungeonName = dungeonName;
        this.id = dungeonName;
        this.entityCount = 0;
        this.randomSeed = randomSeed;
        gameMode = gameMode.toLowerCase();
        if (gameMode.equals("hard")) {
            this.gamemode = new Hard();
        } else if (gameMode.equals("standard")) {
            this.gamemode = new Standard();
        } else {
            this.gamemode = new Peaceful();
        }
        this.inventory = new Inventory();
        this.factory = new NewGameFactory(gamemode, randomSeed);
    }

    /**
     * Constructor for world for a saved game
     * @param dungeonName Name of dungeon
     * @param gameMode gamemode, e.g. standard
     * @param id name of the save game
     * @param randomSeed seed for game
     */
    public World(String dungeonName, String gameMode, String id, int randomSeed) {
        this(dungeonName, gameMode, randomSeed);
        this.id = id;
        this.factory = new LoadGameFactory(gamemode);
    }

    /**
     * Create the entities from the world template
     * @param worldData JSON object containing world template
     * @return world dungeon reponse
     */
    public DungeonResponse buildWorld(JSONObject worldData) {
        JSONArray entities = worldData.getJSONArray("entities");

        for (int i = 0; i < entities.length(); i++) {
            Entity e = factory.createEntity(entities.getJSONObject(i), this);
            addEntity(e);
        }


        // TODO can we put this in a shared method
        if (worldData.has("goal-condition")) {
            JSONObject g = worldData.getJSONObject("goal-condition");
            GoalComponent goal = factory.createGoal(g);
            setGoals(goal);
        }
        else {
            setGoals(null);
        }

        // set up logic observer pattern and trigger any switches
        setUpLogicObservers();
        triggerSwitches();

        movingEntities.forEach((id, me) -> {
            player.subscribePassiveObserver((PlayerPassiveObserver)me);
        });

        if (!(goals == null)) {
            goals.evaluate(this);
        }
        return worldDungeonResponse();
    }

    /**
     * Add new entity to correct storage list
     * @param e Entity to add
     */
    private void addEntity(Entity e) {
        if (e instanceof Player) {
            this.player = (Player) e;
        }
        else if (e instanceof CollectableEntity) {
            collectableEntities.put(e.getId(), (CollectableEntity) e);
        }
        else if (e instanceof MovingEntity) {
            movingEntities.put(e.getId(), (MovingEntity) e);
        }
        else if (e instanceof StaticEntity) {
            staticEntities.put(e.getId(), (StaticEntity) e);
        }
    }

    /**
     * Gets a Goal response
     * @return string goal response
     */
    private String getGoalsResponse() {
        if (!(goals == null)) {
            return goals.remainingGoalString();
        }
        else return null;
    }

    /**
     * Set goals
     */
    private void setGoals(GoalComponent goals) {
        this.goals = goals;
    }

    /**
     * Gets the current character that the player has just battled with
     * @return current battle character
     */
    public MovingEntity getBattleCharacter() {
        return currentBattle.getCharacter();
    }

    /**
     * Obtains and collects the battle rewards from that round of battle
     * @param world current world that the battle occurs in
     */
    private void collectBattleRewards(World world) {
        List<CollectableEntity> battleRewards = factory.dropBattleReward(this);
        for (CollectableEntity e: battleRewards) {
            inventory.collect(e);
        }
    }

    /**
     * World tick which runs everything per tick
     * @param itemUsed if null, it is a movement, item id of item
     * @param movementDirection if null, it is itemUsed, movement direction
     * @return Dungeon response
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if itemUsed is not a bomb, invincibility_potion or an invisibility_potion
        // InvalidActionException if itemUsed is not in the player's inventory

        if (!Objects.isNull(itemUsed) && !(inventory.getType(itemUsed) == null)) {
            if (inventory.getType(itemUsed).equals("bomb")) {
                Bomb b = inventory.getBomb(itemUsed);
                String logic = b.logicString();
                inventory.use(itemUsed);
                PlacedBomb newBomb = (PlacedBomb) (factory.createEntity(player.getX(), player.getY(), "bomb_placed", this, logic));
                staticEntities.put(newBomb.getId(), newBomb);
                // set up observers for this new entity
                setUpLogicObservers();
            } else {
                CollectableEntity potion = inventory.tick(itemUsed);
                if (!Objects.isNull(potion)) {
                    player.addPotion(potion);
                }
            }
        }


        player.tick(movementDirection, this);
        for (MovingEntity me : movingEntities.values()) {
            if (me.getPosition().equals(player.getPosition())) {
                currentBattle = player.battle(me,this, gamemode);
                if (!Objects.isNull(currentBattle)) {
                    currentBattle.battleTick(inventory);
                    if (currentBattle.getPlayerWins()) {
                        collectBattleRewards(this);

                    } else {
                        this.player = null; // will end game in dungeon response
                        // needs to return early
                        return worldDungeonResponse();
                    } // if invisible it will add null
                }

            }
        }

        // collecting the collectable entity if it exists on the current position
        CollectableEntity collectable = getCollectableEntity(player.getPosition());
        if(!Objects.isNull(collectable)) {
            if (inventory.collect(collectable)) {
                collectableEntities.remove(collectable.getId());
            }
        }
        if (inventory.hasItem("sceptre")) inventory.tickSceptre();

        // now move all entities
        for (MovingEntity me: movingEntities.values()) {
            if (!Objects.isNull(currentBattle) && currentBattle.getCharacter().equals(me)) continue;
            me.move(this);
            if (me.getPosition().equals(player.getPosition())) {
                currentBattle = player.battle(me,this, gamemode);
                if (!Objects.isNull(currentBattle)) {
                    currentBattle.battleTick(inventory);
                    if (currentBattle.getPlayerWins()) {
                        collectBattleRewards(this);

                    } else {
                        this.player = null; // will end game in dungeon response
                        // needs to return early
                        return worldDungeonResponse();
                    } // if invisible it will add null
                }
            }
        }

        // spawn relevant enemies at the specified tick intervals
        List<Entity> newEntities = factory.tick(this);
        

        for (Entity e: newEntities) {
            addEntity(e);
            if (e instanceof MovingEntity) {
                player.subscribePassiveObserver((PlayerPassiveObserver)e);
            }
        }

        // reset logic circuits then trigger
        tickLogic();
        tickBombs();

        // Now evaluate goals. Goal should never be null, but add a check incase there is an error in the input file

        if (!Objects.isNull(currentBattle)) {
            movingEntities.remove(currentBattle.getCharacter().getId());
            player.unsubscribePassiveObserver((PlayerPassiveObserver)currentBattle.getCharacter());
            currentBattle = null;
        }
        if (!(goals == null)){
            goals.evaluate(this);
        }

        tickCount++;
        return worldDungeonResponse();
    }
    
    /**
     * Triggers any bombs
     */
    private void tickBombs() {
        List<PlacedBomb> bombs = new ArrayList<>();
        for (StaticEntity se : staticEntities.values()) {
            if (se instanceof PlacedBomb) {
                PlacedBomb b = (PlacedBomb) se;
                if (b.isActivated()) {
                    bombs.add(b);
                }
            }
        }
        for (PlacedBomb b : bombs) {
            b.detonate(this);
        }
    }

    /**
     * Resets and triggers logic circuits for this tick
     */
    private void tickLogic() {
        for (StaticEntity se : staticEntities.values()) {
            if (se instanceof Logic) {
                ((Logic) se).reset();
            }
        }
        triggerSwitches();
    }


    /**
     * Helper method to set up the logic component observer pattern
     */
    private void setUpLogicObservers() {
        for (StaticEntity se : staticEntities.values()) {
            if (se instanceof Logic) {
                observeAdjacentLogicComponents(se);
            }
        }
    }

    /**
     * Add entity to relevant logic component observer lists
     * @param e entity to make observer
     */
    private void observeAdjacentLogicComponents(StaticEntity e) {
        List<Position> adjPositions = e.getPosition().getCardinallyAdjacentPositions();

        for (Position p : adjPositions) {
            List<StaticEntity> statics = getStaticEntitiesAtPosition(p);
            for (StaticEntity se : statics) {
                // if (se.equals(e)) {
                if (se.getId().equals(e.getId())) {

                    // make sure to not observe itself
                }
                // all logic components should observe switches and wires
                else if (se instanceof FloorSwitch || se instanceof Wire) {
                    ((Logic) se).addObserver(((Logic) e).getLogic());
                }
            }
        }
    }


    /**
     * Find a valid spider spawn
     * @param position position we are checking
     * @return boolean true if found
     */
    public boolean validSpiderSpawnPosition(Position position) {
        StaticEntity se = getStaticEntity(position);
        MovingEntity me = getCharacter(position);

        // if there is a static entity and its a boulder OR there is already a moving entity OR player is there, NOT VALID
        if ((!(se == null) && (se instanceof Boulder)) || !(me == null) || (player.getPosition().equals(position))) {
            return false;
        }
        return true;
    }
    /**
     * Find a valid hydra spawn
     * @param position position we are checking
     * @return boolean true if found
     */
    public boolean validHydraSpawnPosition(Position position) {
        StaticEntity se = getStaticEntity(position);
        MovingEntity me = getCharacter(position);

        // if there is a static entity OR there is already a moving entity OR player is there, NOT VALID
        if (!(se == null) || !(me == null) || (player.getPosition().equals(position))) {
            return false;
        }
        return true;
    }
    
    /** 
     * Checks whether a given position is a valid zombie spawn position
     * @param position Position to check
     * @return true if the position giveen is valid, otherwise false
     */
    public boolean validZombieSpawnPosition(Position position) {
        StaticEntity se = getStaticEntity(position);
        MovingEntity me = getCharacter(position);

        // if there is a static entity higher than layer 0 OR there is already a moving entity OR player is there, NOT VALID
        if ((!(se == null) && (se.getPosition().getLayer() > 0) || !(me == null) || (player.getPosition().equals(position)))) {
            return false;
        }
        return true;
    }

    /**
     * find number of spiders in world
     * @return number of spiders
     */
    public int currentSpiders() {
        int currSpiders = 0;
        for (MovingEntity m : movingEntities.values())  {
            if (m instanceof Spider) {
                currSpiders++;
            }
        }
        return currSpiders;
    }

    /**
     * Interacts with a mercenary (where the character bribes the mercenary) or a zombie spawner (where the character destroys the zombie spawner)
     * @param entityId id of entity to interact with
     * @return dungeon response
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if entityId is not a valid entityId
        // InvalidAction Exception if:
        // * the player is not cardinally adjacent to the given entity,
        // * the player does not have any gold and attempts to bribe a mercenary
        // * the player does not have a weapon and attempts to destroy a spawner
        if (movingEntities.containsKey(entityId)) {
            MovingEntity e = movingEntities.get(entityId);
            if (!(e instanceof MercenaryComponent)) {
                throw new IllegalArgumentException();
            } else {
                ((MercenaryComponent) e).interact(this);
            }
        } else if (staticEntities.containsKey(entityId)) {
            StaticEntity e = staticEntities.get(entityId);
            if (!(e instanceof ZombieToastSpawn)) {
                throw new IllegalArgumentException();
            } else {
                ((ZombieToastSpawn)e).interact(this);
            }
        } else {
            throw new IllegalArgumentException();
        }

        return worldDungeonResponse();
    }

    /**
     * Builds the given entity where buildable is one of "bow" or "shield"
     * @param buildable bow or shield
     * @return dungeon response
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if buildable is not one of bow or shield
        // InvalidActionException if the player does not have sufficient items to craft the buildable
        if (buildable.equals("midnight_armour") && numMovingEntity("zombie_toast") != 0) {
            throw new InvalidActionException("There are zombies alive");
        }
        inventory.craft(buildable, String.valueOf(incrementEntityCount()));
        return worldDungeonResponse();
    }

    // Return a dungeon response for the current world
    public DungeonResponse worldDungeonResponse() {
        return new DungeonResponse(id, dungeonName, getEntityResponses(), getInventoryResponse(), inventory.getBuildable(), getGoalsResponse());
    }

    /**
     * Returns the static entity that exists in the dungeon at position p (if one exists)
     * Note, that this only looks at layer 1 (will not return for floor switch, exit)
     * @param p position to check
     * @return Static entity at position p
     */
    public StaticEntity getStaticEntity(Position p) {
        for (StaticEntity s: staticEntities.values()) {
            if (s.getPosition().equals(p) && s.getLayer() == 1)  {
                return s;
            }
        }
        return null;
    }

    /**
     * get a list of static entities at a position, useful for switch and boulders
     * @param p position in question
     * @return list of entities at position p
     */
    public List<StaticEntity> getStaticEntitiesAtPosition(Position p) {
        return staticEntities.values().stream().filter(x  -> x.getPosition().equals(p)).collect(Collectors.toList());
    }

    /**
     * Return sthe Collectable entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Collectable entity at position p
     */
    public CollectableEntity getCollectableEntity(Position p) {
        // I am not very good at streams, so not sure if there is an elegant way to use them to return only 1 object from a list
        for (CollectableEntity e: collectableEntities.values()) {
            if (e.getPosition().equals(p)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Return the character that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return MovingEntity at position p
     */
    public MovingEntity getCharacter(Position p){
        for (MovingEntity c: movingEntities.values()) {
            if (c.getPosition().equals(p)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets the list of battles that exit currently in the world
     * @return list of current battles in world
     */
    public Battle getBattle() {
        return currentBattle;
    }

    /**
     * Gets the Player object of the world
     * @return Player of the world
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if a given Collectable item is in the players inventory
     * @param item collectable item to check for in inventory
     * @return true if the item is in the players inventory, false otherwise
     */
    public boolean inInventory(CollectableEntity item) {
        return inventory.inInventory(item.getId());
    }

    public boolean inInventory(String itemType) {
        return inventory.hasItem(itemType);
    }

    /**
     * Checks if a key exists in inventory and returns object
     * @param keyColour keyColour of the key to find
     * @return Key if exists in inventory, else false
     */
    public Key keyInInventory(int keyColour) {
        return inventory.keyInInventory(keyColour);
    }

    /**
     * Increments entity count and returns count for current entity id
     * Gets the current entity count from the factory, increments,
     * and sets factory count accordingly
     * @return int id
     */
    public int incrementEntityCount() {
        this.entityCount = factory.getEntityCount() + 1;
        factory.setEntityCount(entityCount);
        return this.entityCount;
    }

    /**
     * Set the current entity count
     * @param entityCount
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
        factory.setEntityCount(entityCount);
    }
    /**
     * Gets all entities entity responses
     * @return list of entity responses
     */
    public List<EntityResponse> getEntityResponses() {
        List<EntityResponse> entityResponses = new ArrayList<>();

        if (!(player == null)){
            entityResponses.add(player.getEntityResponse());
        }
        if (!movingEntities.isEmpty()) entityResponses.addAll(movingEntities.values().stream().map(MovingEntity::getEntityResponse).collect(Collectors.toList()));
        if (!staticEntities.isEmpty()) entityResponses.addAll(staticEntities.values().stream().map(StaticEntity::getEntityResponse).collect(Collectors.toList()));
        if (!collectableEntities.isEmpty()) entityResponses.addAll(collectableEntities.values().stream().map(CollectableEntity::getEntityResponse).collect(Collectors.toList()));

        return entityResponses;
    }

    /**
     * get the world inventory response
     * @return list of item responses
     */
    public List<ItemResponse> getInventoryResponse(){
        return inventory.getInventoryResponse();
    }

    /**
     * Remove all entities from the given positions (except the player)
     * @param position position to remove entities from
     */
    public void detonate(Position position) {

        List<Entity> collectable = new ArrayList<>(collectableEntities.values());
        removeEntities(position, collectable);

        List<Entity> statics = new ArrayList<>(staticEntities.values());
        removeEntities(position, statics);

        List<Entity> moving = new ArrayList<>(movingEntities.values());
        // remove player
        moving.remove(player);
        removeEntities(position, moving);
    }

    /**
     * Remove all entities in given list at given position
     * @param position position to remove entities from
     * @param entities list of entities to check
     */
    public void removeEntities(Position position, List<Entity> entities) {
        List<Entity> toRemove = new ArrayList<>();

        for (Entity e : entities) {
            if (e.getPosition().equals(position)) {
                toRemove.add(e);
            }
        }

        for (Entity e : toRemove) {
            entities.remove(e);
            collectableEntities.remove(e.getId());
            staticEntities.remove(e.getId());
            movingEntities.remove(e.getId());
        }
    }

    public Map<String, StaticEntity> getStaticEntities() {
        return this.staticEntities;
    }

    public Map<String, MovingEntity> getMovingEntities() {
        return this.movingEntities;
    }


    public Map<String, CollectableEntity> getCollectibleEntities() {
        return this.collectableEntities;
    }

    /**
     * Calls use for thr specified item id in inventory
     * @param itemId the item to be used
     */
    public void use(String itemId) {
        inventory.use(itemId);
    }

    /**
     * Checks if the player has a weapon in inventory
     * @return true if there is a weapon, otherwise false
     */
    public boolean playerHasWeapon(){
        return inventory.hasWeapon();
    }



    /**
     * Saves the game to a json based on existing information
     * @return Json object of saved game state
     */
    public JSONObject saveGame() {
        JSONObject worldJSON = new JSONObject();

        worldJSON.put("gamemode", gamemode.getGameModeType());
        worldJSON.put("tick-count", tickCount);
        worldJSON.put("dungeon-name", dungeonName);
        worldJSON.put("entity-count", entityCount);
        worldJSON.put("inventory", inventory.saveGameJson());
        worldJSON.put("player", player.saveGameJson());
        worldJSON.put("moving-entities", movingEntitySaveGameJson());
        worldJSON.put("static-entities", staticEntitySaveGameJson());
        worldJSON.put("collectable-entities", collectableEntityGameJson());

        if (!(currentBattle==null) && currentBattle.isActiveBattle()) {
            worldJSON.put("current-battle", currentBattle.getCharacter().getId());
        }
        if (!(goals == null)) {
            worldJSON.put("goal-condition", goals.saveGameJson());
        }

        if (inventory.getSceptre() != null) {
            worldJSON.put("controlled", inventory.getSceptre().getMindControlled());
        }

        return worldJSON;
    }

    /**
     * Save all static entities
     * @return json of static entities
     */
    private JSONArray staticEntitySaveGameJson() {
        JSONArray staticEntitiesJSON = new JSONArray();
        staticEntities.values().stream()
                                .map(StaticEntity::saveGameJson)
                                .forEach(x -> staticEntitiesJSON.put(x));
        return staticEntitiesJSON;
    }

    /**
     * Save all moving entities
     * @return json of moving entities
     */
    private JSONArray movingEntitySaveGameJson() {
        JSONArray movingEntitiesJSON = new JSONArray();
        movingEntities.values().stream()
                                .map(MovingEntity::saveGameJson)
                                .forEach(x -> movingEntitiesJSON.put(x));
        return movingEntitiesJSON;
    }

    /**
     * Save all collectable entities
     * @return json of collectable entities
     */
    private JSONArray collectableEntityGameJson() {
        JSONArray collectableEntitiesJSON = new JSONArray();
        collectableEntities.values().stream()
                                .map(CollectableEntity::saveGameJson)
                                .forEach(x -> collectableEntitiesJSON.put(x));
        return collectableEntitiesJSON;
    }

    /**
     * Get the number of items of given type in inventory
     * @param itemType Type of the item
     * @return the number of itemType available
     */
    public int numItemInInventory(String itemType) {
        return inventory.numItem(itemType);
    }

    /**
     * Uses an item in the inventory of the given type (if it exists)
     * @param type type of the item we want to use
     */
    public void useByType(String type) {
        inventory.useByType(type);
    }

    /**
     * Uses the sceptre in the inventory 
     * @param m the MercenaryComponent to be mind controlled
     */
    public void useSceptre(MercenaryComponent m) {
        inventory.useSceptre(m);
    }

    /**
     * Uses the sceptre in the inventory 
     * @param m the MercenaryComponent to be mind controlled
     * @param duration the duration left for the mind control effect
     */
    public void useSceptre(MercenaryComponent m, int duration) {
        inventory.useSceptre(m, duration);
    }

    /**
     * Get the current tick count
     * @return tickcount
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Get the name of the dungeon
     * @return dungeonName
     */
    public String getDungeonName() {
        return dungeonName;
    }

    /**
     * Set the id of the world
     * @param id the id to set
     */
    public void setId(String id) {
        this.id  = id;
    }

    /**
     * Get the id of the world
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Loads a saved game with all entities
     * @param gameData the data from the save file
     */
    public void buildWorldFromFile(JSONObject gameData) {
        int tickNo = gameData.getInt("tick-count");
        int entityNo = gameData.getInt("entity-count");

        
        // set the world and factor counters appropriately
        setEntityCount(entityNo);
        factory.setEntityCount(entityNo);
        setTickCount(tickNo);
        factory.setTickCount(tickNo);


        if (gameData.has("goal-condition")) {
            JSONObject g = gameData.getJSONObject("goal-condition");
            GoalComponent goal = factory.createGoal(g);
            setGoals(goal);
        }
        else {
            setGoals(null);
        }

        JSONArray inventoryItems = gameData.getJSONArray("inventory");
        for (int i = 0; i < inventoryItems.length(); i++) {
            Entity e = factory.createEntity(inventoryItems.getJSONObject(i), this);
            inventory.collect((CollectableEntity) e);
        }

        JSONObject playerObj = gameData.getJSONObject("player");
        Entity player = factory.createEntity(playerObj, this);
        this.player = (Player) player;


        JSONArray movingEntitiesItems = gameData.getJSONArray("moving-entities");
        for (int i = 0; i < movingEntitiesItems.length(); i++) {
            Entity e = factory.createEntity(movingEntitiesItems.getJSONObject(i), this);
            movingEntities.put(e.getId(), (MovingEntity) e);
        }

        JSONArray staticEntitiesItems = gameData.getJSONArray("static-entities");
        for (int i = 0; i < staticEntitiesItems.length(); i++) {
            Entity e = factory.createEntity(staticEntitiesItems.getJSONObject(i), this);
            staticEntities.put(e.getId(), (StaticEntity) e);
        }

        JSONArray collectableEntitiesItems = gameData.getJSONArray("collectable-entities");
        for (int i = 0; i < collectableEntitiesItems.length(); i++) {
            Entity e = factory.createEntity(collectableEntitiesItems.getJSONObject(i), this);
            collectableEntities.put(e.getId(), (CollectableEntity) e);
        }

        // trigger any switches with a boulder already on top
        triggerSwitches();

        movingEntities.forEach( (id, me) -> {
            this.player.subscribePassiveObserver((PlayerPassiveObserver)me);
        });

        if (!(goals == null)) {
            goals.evaluate(this);
        }

        int entityCount = factory.getEntityCount();

        this.factory = new NewGameFactory(gamemode, randomSeed, player.getPosition());
        factory.setEntityCount(entityCount);
        factory.setTickCount(tickCount);

        if (gameData.has("controlled")) {
            JSONArray mindControlledEntities = gameData.getJSONArray("controlled");
            for (int i = 0; i < mindControlledEntities.length(); i++) {
                JSONObject obj = mindControlledEntities.getJSONObject(i);
                MercenaryComponent m = (MercenaryComponent) movingEntities.get(obj.getString("id"));
                int duration = obj.getInt("duration");
                useSceptre(m, duration);
            }
        }
    }

    /**
     * Helper function to set switches as triggered when a map is loaded.
     */
    private void triggerSwitches() {
        for (StaticEntity se : staticEntities.values()) {
            if (se instanceof FloorSwitch) {
                StaticEntity entity = getStaticEntity(se.getPosition());
                if (entity instanceof Boulder) {
                    ((FloorSwitch) se).trigger(this);
                }
            }
        }

    }

    /**
     * Get the players position
     * @return Players position
     */
    public Position getPlayerPosition() {
        return player.getPosition();
    }

    /**
     * Set the tickcount of the world and the factory
     * @param tickCount tickcount to set
     */
    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
        factory.setTickCount(tickCount);
    }

    /**
     * Returns the amount of the provided enitity type currently present within the world
     * @param entityType the entity type to be checked
     * @return the amount of the provided entity type
     */
    public int numMovingEntity(String entityType) {
        int count = 0;
        for (MovingEntity entity : movingEntities.values()) {
            if (entity.getType() == entityType) {
                count++;
            }
        }
        return count;
    }
 
    public double getDistance(Position position) {
        List<StaticEntity> l = getStaticEntitiesAtPosition(position);
        for (StaticEntity se : l) {
            if (se.getType().equals("swamp_tile")) {
                SwampTile s = (SwampTile)se;
                return s.getMovementFactor();
            }
        }
        return 1.0;
    }

    public int getXBound() {
        return Math.max(player.getX(), factory.getHighestX());
    }

    public int getYBound() {
        return Math.max(player.getY(), factory.getHighestY());
    }

    public int getXBoundN() {
        return Math.min(player.getX(), 0);
    }

    public int getYBoundN() {
        return Math.min(player.getY(), 0);
    }

    /**
     * Checks if the sceptre is useable (off cooldown)
     * @return true if off cooldown, false otherwise
     */
    public boolean useableSceptre() {
        return inventory.usableSceptre();
    }

    /**
     * Generates a maze dungeon with the specified start and end point using the randomized prims algorithm
     * @param xStart the x coordinate of the start position
     * @param yStart the y coordinate of the start position
     * @param xEnd the x coordinate of the end position
     * @param yEnd the y coordinate of the end position
     * @return the DungeonResponse corresponding to the generated dungeon
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd) {

        JSONObject generateMaze = RandomizedPrims(xStart, yStart, xEnd, yEnd);
        JSONObject goalCondition = new JSONObject();
        goalCondition.put("goal", "exit");
        generateMaze.put("goal-condition", goalCondition);
        
        return buildWorld(generateMaze);
        
    }
    
    /**
     * Generates a maze with the randomizes prims algorithm
     * @param xStart the x coordinate of the start position
     * @param yStart the y coordinate of the start position
     * @param xEnd the x coordinate of the end position
     * @param yEnd the y coordinate of the end position
     * @return the JSONObject containing all walls of the maze
     */
    public JSONObject RandomizedPrims(int xStart, int yStart, int xEnd, int yEnd) {
        Boolean[][] maze = new Boolean[DEFAULT_HEIGHT][DEFAULT_WIDTH];
        for(int i = 0; i < DEFAULT_HEIGHT; i++) {
            for(int j = 0; j < DEFAULT_WIDTH; j++){
                maze[i][j] = false;
            }
        }

        maze[xStart][yStart] = true;
        List<Position> options = new ArrayList<>();
        Position start = new Position(xStart, yStart);

        List<Position> adjacents = start.get2AdjacentPosition();

        adjacents.removeIf( pos -> isBoundary(pos.getX(), pos.getY()));
        adjacents.removeIf( pos -> maze[pos.getX()][pos.getY()]);
        options.addAll(adjacents);

        while (!options.isEmpty()) {
            Random rand = new Random();
            Position next = options.remove(rand.nextInt(options.size()));

            List<Position> neighbours = next.get2AdjacentPosition();
            neighbours.removeIf( pos -> isBoundary(pos.getX(), pos.getY()));
            neighbours.removeIf( pos -> !maze[pos.getX()][pos.getY()]);

            if (!neighbours.isEmpty()) { // may need to also remove
                Position neighbour = neighbours.get(rand.nextInt(neighbours.size()));
                maze[next.getX()][next.getY()] = true;
                Position inbetween = positionInbetween(next.getX(), next.getY(), neighbour.getX(), neighbour.getY());
                maze[inbetween.getX()][inbetween.getY()] = true;
                maze[neighbour.getX()][neighbour.getY()] = true;
            }

            List<Position> neighbours2 = next.get2AdjacentPosition();
            neighbours2.removeIf( pos -> isBoundary(pos.getX(), pos.getY()));
            neighbours2.removeIf( pos -> maze[pos.getX()][pos.getY()]);
            Set<Position> set = new HashSet<>(options);
            set.addAll(neighbours2);
            options.clear();
            options.addAll(set);
        }

        if (!maze[xEnd][yEnd]) {
            maze[xEnd][yEnd] = true;
            Position mazeEnd = new Position(xEnd, yEnd);
            List<Position> neighbours = mazeEnd.getAdjacentPositions();
            neighbours.removeIf( pos -> isBoundary(pos.getX(), pos.getY()));
            if (!neighbourCellEmpty(neighbours, maze)) {
                Random rand = new Random();
                Position neighbour = neighbours.get(rand.nextInt(neighbours.size()));
                maze[neighbour.getX()][neighbour.getY()] = true;
            }
        }

        return createJSONfromMaze(maze, xStart, yStart, xEnd, yEnd);
    }

    /**
     * Checks if the provided position is a boundary
     * @param x coordinate of the position to be checked
     * @param y coordinate of the position to be checked
     * @return true if the position is a boundary, false otherwise
     */
    private boolean isBoundary(int x, int y) {
        if (x <= 0 || x >= DEFAULT_HEIGHT - 1) {
            return true;
        }
        if (y <= 0 || y >= DEFAULT_WIDTH - 1) {
            return true;
        }
        return false;
    }

    /**
     * Provides the position between the two provided locations
     * @param xNext the x coordinate of the next position
     * @param yNext the y coordinate of the next position
     * @param xNeigh the x coordinate of the neighbour position
     * @param yNeigh the y coordinate of the neighbour position
     * @return the position between the two provided locations
     */
    private Position positionInbetween(int xNext, int yNext, int xNeigh, int yNeigh) {
        if (xNext == xNeigh) {
            if (yNext > yNeigh) {
                return new Position(xNext, yNext - 1);
            } else {
                return new Position(xNext, yNext + 1);
            }
        } else {
            if (xNext > xNeigh) {
                return new Position(xNext - 1, yNext);
            } else {
                return new Position(xNext + 1, yNext);
            }
        }
    }
    
    /**
     * Checks if a neighbour cell is empty
     * @param neighbours list of all positions that are neighbours
     * @param maze the maze containing all walls and empty spaces
     * @return true if there is an empty neighbour, false otherwise
     */
    private boolean neighbourCellEmpty(List<Position> neighbours, Boolean[][] maze) {
        for (Position neighbour : neighbours) {
            if (maze[neighbour.getX()][neighbour.getY()]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a JSONObject of the generated maze
     * @param maze the maze containing all walls and empty spaces denoted by a boolean
     * @param xStart the x coordinate of the start position
     * @param yStart the y coordinate of the start position
     * @param xEnd the x coordinate of the end position
     * @param yEnd the y coordinate of the end position
     * @return the JSONObject containing all walls of the maze
     */
    private JSONObject createJSONfromMaze(Boolean[][] maze, int xStart, int yStart, int xEnd, int yEnd ) {
        JSONObject mazeJSON = new JSONObject();
        mazeJSON.put("width", DEFAULT_WIDTH);
        mazeJSON.put("height", DEFAULT_HEIGHT);
        JSONArray entities = new JSONArray();
        for(int i = 0; i < DEFAULT_HEIGHT; i++) {
            for(int j = 0; j < DEFAULT_WIDTH; j++){
                if(!maze[i][j]) {
                    JSONObject wall = new JSONObject();
                    wall.put("x", j);
                    wall.put("y", i);
                    wall.put("type", "wall");
                    entities.put(wall);
                }
            }
        }
        JSONObject playerJSON = new JSONObject();
        playerJSON.put("x", xStart);
        playerJSON.put("y", yStart);
        playerJSON.put("type", "player");
        entities.put(playerJSON);

        JSONObject exitJSON = new JSONObject();
        exitJSON.put("x", xEnd);
        exitJSON.put("y", yEnd);
        exitJSON.put("type", "exit");
        entities.put(exitJSON);

        mazeJSON.put("entities", entities);

    
        return mazeJSON;
    }

}
