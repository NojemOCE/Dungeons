package dungeonmania;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.crypto.AlgorithmMethod;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.goal.*;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.*;
import dungeonmania.staticEntity.*;
import dungeonmania.util.*;
import dungeonmania.gamemode.*;
import dungeonmania.movingEntity.*;
import dungeonmania.movingEntity.States.SwampState;
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.factory.*;
import dungeonmania.util.Position;

public class World {

    private Inventory inventory;
    private Gamemode gamemode;
    private Player player;
    private String id; //TODO only ever set if game is saved?
    private GoalComponent goals;
    private Map<String, CollectableEntity> collectableEntities = new HashMap<>();
    private Map<String, StaticEntity> staticEntities = new HashMap<>();
    private Map<String, MovingEntity> movingEntities = new HashMap<>();
    private int entityCount;
    private Battle currentBattle;
    private String dungeonName;
    private Factory factory;
    private int randomSeed;


    static final int MAX_SPIDERS = 6;
    static final int SPIDER_SPAWN = 20;
    static final int MECERNARY_SPAWN = 25;
    static final double MERCENARY_ARMOUR_DROP = 0.4;
    static final double ZOMBIE_ARMOUR_DROP = 0.2;
    static final double ONE_RING_DROP = 0.1;
    private int tickCount = 1;
    
    /**
     * Constructor for world that takes the string of the dungeon name to build 
     * and a string for the gamemode (Standard, Peaceful, Hard)
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
        this.factory = new NewGameFactory(gamemode, (new Random(randomSeed)).nextInt());


    }

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

            // move increment entity count to factory
            // if collectable, add to colletable etc
        }


        // TODO can we put this in a shared method
        if (worldData.has("goal-condition")) {
            JSONObject g = worldData.getJSONObject("goal-condition");
            //GoalComponent goal = createGoal(g);
            GoalComponent goal = factory.createGoal(g);
            setGoals(goal);
        }
        else {
            setGoals(null);
        }

        triggerSwitches();
        movingEntities.forEach((id, me) -> {
            player.subscribePassiveObserver((PlayerPassiveObserver)me);
        });

        if (!(goals == null)) {
            goals.evaluate(this);
        }
        return worldDungeonResponse();
    }


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
    private void setGoals(GoalComponent goals) {
        this.goals = goals;
    }

    /**
     * Drops armour:
     * 20% of the time if the player has beaten a zombie
     * 40% of the time if the player has beaten a mercenary
     * 
     * Drops the one ring:
     * 10% of the time
     * 
     * If an item is dropped, it is automatically added to the players inventory
     */
    private void dropBattleReward(){
        Position characterPos = currentBattle.getCharacter().getPosition();
        int charX = characterPos.getX();
        int charY = characterPos.getY();


        if (currentBattle.getCharacter() instanceof Mercenary) {
            Random ran = new Random(randomSeed);
            int next = ran.nextInt(10);
            if (10 * MERCENARY_ARMOUR_DROP > next)  {
                // return an armour
                Armour armour = new Armour(charX, charY, "armour" + String.valueOf(incrementEntityCount()));
                inventory.collect(armour);
            }
        }

        else if (currentBattle.getCharacter() instanceof Zombie) {
            Random ran = new Random(randomSeed);
            int next = ran.nextInt(10);
            if (10 * ZOMBIE_ARMOUR_DROP > next)  {
                // return an armour
                Armour armour = new Armour(charX, charY, "armour" + String.valueOf(incrementEntityCount()));
                inventory.collect(armour);
            }
        }

        Random ran = new Random(randomSeed);
        int next = ran.nextInt(10);
        if (10 * ONE_RING_DROP > next)  {
            // return the one ring
            OneRing oneRing = new OneRing(charX, charY, "one_ring" + String.valueOf(incrementEntityCount()));
            inventory.collect(oneRing);
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

        if (!Objects.isNull(itemUsed)) {
            if (!(inventory.getType(itemUsed) == null) && inventory.getType(itemUsed).equals("bomb")) {
                inventory.use(itemUsed);
                PlacedBomb newBomb = new PlacedBomb(player.getX(), player.getY(), "bomb" + String.valueOf(incrementEntityCount()));
                staticEntities.put(newBomb.getId(), newBomb);
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
                        dropBattleReward();
    
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

        // now move all entities 
        for (MovingEntity me: movingEntities.values()) {
            if (!Objects.isNull(currentBattle) && currentBattle.getCharacter().equals(me)) continue;
            me.move(this);
            if (me.getPosition().equals(player.getPosition())) {
                currentBattle = player.battle(me,this, gamemode);
                if (!Objects.isNull(currentBattle)) {
                    currentBattle.battleTick(inventory);
                    if (currentBattle.getPlayerWins()) {
                        dropBattleReward();
    
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
        inventory.craft(buildable, String.valueOf(incrementEntityCount()));
        return worldDungeonResponse();
    }

    // Return a dungeon response for the current world
    public DungeonResponse worldDungeonResponse(){
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


    /**
     * Checks if a key exists in inventory and returns object
     * @param keyColour keyColour of the key to find
     * @return Key if exists in inventory, else false
     */
    public Key keyInInventory(int keyColour) {
        return inventory.keyInInventory(keyColour);
    }

    /**
     * Increments entity count for entity id
     * @return int id
     */
    public int incrementEntityCount() {
        this.entityCount++;
        return this.entityCount;
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
     * Check if co-ordinates are in bounds
     * @param x x co-ord
     * @param y y co-ord
     * @return true if in bound
     */
    // public boolean inBounds(int x, int y) {
    //     return !(x < 0 || x >= highestX || y < 0 || y >= highestY);
    // }


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
    
    public int numItemInInventory(String itemType) {
        return inventory.numItem(itemType);
    }

    /**
     * Uses an item in the inventoryof the given type (if it exists)
     * @param type type of the item we want to use
     */
    public void useByType(String type) {
        inventory.useByType(type);
    }

    public int getTickCount() {
        return tickCount;
    }

    public String getDungeonName() {
        return dungeonName;
    }
    public void setId(String id) {
        this.id  = id;
    }
    public String getId() {
        return id;
    }

    /**
     * Loads a saved game with all entities
     */
    public void buildWorldFromFile(JSONObject gameData) {
        int tickNo = gameData.getInt("tick-count");
        int entityNo = gameData.getInt("entity-count");
        
        setEntityCount(entityNo);
        setTickCount(tickNo);

        // TODO can we put this in a shared method
        if (gameData.has("goal-condition")) {
            JSONObject g = gameData.getJSONObject("goal-condition");
            //GoalComponent goal = createGoal(g);
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
        //TODO get merc list
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

        Random ran = new Random(randomSeed);

        this.factory = new NewGameFactory(gamemode, ran.nextInt());
        factory.setEntityCount(entityCount);

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

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
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
    
}
