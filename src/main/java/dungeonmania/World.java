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
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;




// TODO: remember to implement all the observer interfaces as we go
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

    static final int MAX_SPIDERS = 6;
    static final int SPIDER_SPAWN = 20;
    static final int MECERNARY_SPAWN = 25;
    static final double MERCENARY_ARMOUR_DROP = 0.4;
    static final double ZOMBIE_ARMOUR_DROP = 0.2;
    static final double ONE_RING_DROP = 0.1;
    private int tickCount = 0;
    private int highestX = 5;
    private int highestY = 5;
    
    /**
     * Constructor for world that takes the string of the dungeon name to build 
     * and a string for the gamemode (Standard, Peaceful, Hard)
     */
    public World(String dungeonName, String gameMode) {
        this.dungeonName = dungeonName;
        this.id = dungeonName;
        this.entityCount = 0;
        if (gameMode.equals("Hard")) {
            this.gamemode = new Hard();
        } else if (gameMode.equals("Standard")) {
            this.gamemode = new Standard();
        } else {
            this.gamemode = new Peaceful();
        }
        this.inventory = new Inventory();
    }

    public World(String dungeonName, String gameMode, String id) {
        this(dungeonName, gameMode);
        this.id = id;
    }

    /**
     * Create the entities from the world template
     * @param worldData JSON object containing world template
     * @return world dungeon reponse
     */
    public DungeonResponse buildWorld(JSONObject worldData) {
        JSONArray entities = worldData.getJSONArray("entities");

        for (int i = 0; i < entities.length(); i++) {
            createEntity(entities.getJSONObject(i), String.valueOf(incrementEntityCount()));
        }
        
        // TODO can we put this in a shared method
        if (worldData.has("goal-condition")) {
            JSONObject g = worldData.getJSONObject("goal-condition");
            GoalComponent goal = createGoal(g);
            setGoals(goal);
        }
        else {
            setGoals(null);
        }

        triggerSwitches();
        movingEntities.forEach((id, me) -> {
            player.subscribePassiveObserver((PlayerPassiveObserver)me);
        });
        return worldDungeonResponse();
    }

    /**
     * Gets the largest bound of the map
     * @param x x co-ordinate
     * @param y y co-ordinate
     */
    private void updateBounds(int x, int y)  {
        if (x > highestX) {
            highestX = x;
        }
        if (y > highestY) {
            highestY = y;
        }
    }
    
    /**
     * Creates an entity from JSONObject
     * @param obj Json object
     * @param id id of entity
     */
    private void createEntity(JSONObject obj, String id) {
        int x = (int)obj.get("x");
        int y = (int)obj.get("y");


        updateBounds(x, y);

        String type = obj.getString("type");

        id = type + id;

        if (type.equals("wall")) {

            Wall e = new Wall(x, y, id);

            staticEntities.put(e.getId(), e);

        } else if (type.equals("exit")) {
            Exit e = new Exit(x,y,id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("switch")) {
            FloorSwitch e = new FloorSwitch(x, y, id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("boulder")) {
            Boulder e = new Boulder(x, y, id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("door")) {
            int key = (int)obj.get("key");
            Door e = new Door(x, y, id, key);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("portal")) {
            Portal e;

            String colour = obj.getString("colour");

            if (staticEntities.containsKey(colour)) {
                e = new Portal(x, y, colour + "2", colour, (Portal) staticEntities.get(colour));
            } else {
                e = new Portal(x, y, colour, colour);
            }
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("zombie_toast_spawner")) {
            ZombieToastSpawn e = new ZombieToastSpawn(x, y, id);
            staticEntities.put(e.getId(), e);   
        } else if (type.equals("player")) {
            Player e = new Player(x, y, id, new HealthPoint(gamemode.getStartingHP()));
           
            this.player = e;
        } 
        
        else if (type.equals("spider")) {
            Spider e = new Spider(x, y, id);
            movingEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("zombie_toast")) {
            Zombie e = new Zombie(x, y, id);
            movingEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("mercenary")) {
            Mercenary e = new Mercenary(x, y, id);

            movingEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("treasure")) {
            Treasure e = new Treasure(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("key")) {
            int key = (int)obj.get("key");
            Key e = new Key(x, y, id, key);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(x, y, id, gamemode.isInvincibilityEnabled());
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invisibility_potion")) {
            InvisibilityPotion e = new InvisibilityPotion(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("sword")) {
            Sword e = new Sword(x, y, id);
            collectableEntities.put(e.getId(), e);
        }
        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(x, y, id);
            collectableEntities.put(e.getId(), e);
        }

    }
    
    /**
     * Create Goal from json
     * @param goal goal json
     * @return Goal component
     */
    private GoalComponent createGoal(JSONObject goal) {
        String currGoal = goal.getString("goal");

        // Will return null if the goal is not exit,enemies,treasure, AND or OR
        if (currGoal.equals("exit")) {
            return new ExitGoal(currGoal);
        }
        else if (currGoal.equals("enemies")) {
            return new EnemiesGoal(currGoal);
        }
        else if (currGoal.equals("boulders")) {
            return new BoulderGoals(currGoal);
        }
        else if (currGoal.equals("treasure")) {
            return new TreasureGoals(currGoal);
        }
        else if (currGoal.equals("AND")) {
            AndGoal andGoal = new AndGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                andGoal.addSubGoal(subGoal);
            }
            return andGoal; 
        }
        else if (currGoal.equals("OR")) {
            OrGoal orGoal = new OrGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                orGoal.addSubGoal(subGoal);
            }
            return orGoal; 
        }

        return null;
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
            Random ran = new Random();
            int next = ran.nextInt(10);
            if (10 * MERCENARY_ARMOUR_DROP > next)  {
                // return an armour
                Armour armour = new Armour(charX, charY, "armour" + String.valueOf(incrementEntityCount()));
                inventory.collect(armour);
            }
        }

        else if (currentBattle.getCharacter() instanceof Zombie) {
            Random ran = new Random();
            int next = ran.nextInt(10);
            if (10 * ZOMBIE_ARMOUR_DROP > next)  {
                // return an armour
                Armour armour = new Armour(charX, charY, "armour" + String.valueOf(incrementEntityCount()));
                inventory.collect(armour);
            }
        }

        Random ran = new Random();
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
        tickSpiderSpawn();
        tickZombieToastSpawn();

        // Now evaluate goals. Goal should never be null, but add a check incase there is an error in the input file
        if (!(goals == null)){
            goals.evaluate(this);
        }
        if (!Objects.isNull(currentBattle)) {
            movingEntities.remove(currentBattle.getCharacter().getId());
            player.unsubscribePassiveObserver((PlayerPassiveObserver)currentBattle.getCharacter());
            currentBattle = null;
        }

        tickCount++;
        return worldDungeonResponse();
    }


    /**
     * Helper function to create a new spider at relevant ticks
     */
    private void tickSpiderSpawn() {
        if (!(tickCount > 0 && tickCount % SPIDER_SPAWN == 0 && currentSpiders() < MAX_SPIDERS)) {
            return;
        }

        Random ran1 = new Random();
        Random ran2 = new Random();

        int x = ran1.nextInt(highestX);
        int y = ran2.nextInt(highestY);
        
        int numChecks = 0;
        while (!validSpiderSpawnPosition(new Position(x,y)) && numChecks < 10) {
            x = ran1.nextInt(highestX);
            y = ran2.nextInt(highestY);
            numChecks++;
        }

        // no valid positions found in reasonable time
        if (numChecks == 10) {
            return;
        }

        Spider newSpider = new Spider(x, y, "spider" + String.valueOf(incrementEntityCount()));
        movingEntities.put(newSpider.getId(), newSpider);
        player.subscribePassiveObserver((PlayerPassiveObserver)newSpider);

    }
    
    /**
     * Find a valid spider spawn
     * @param position position we are checking
     * @return boolean true if found
     */
    private boolean validSpiderSpawnPosition(Position position) {
        StaticEntity se = getStaticEntity(position);
        MovingEntity me = getCharacter(position); 

        // if there is a static entity and its a boulder OR there is already a moving entity OR player is there, NOT VALID
        if ((!(se == null) && (se instanceof Boulder)) || !(me == null) || (player.getPosition().equals(position))) {
            return false;
        }
        return true;
    }

    /**
     * Updates zombie toast spawners
     */
    private void tickZombieToastSpawn() {
        for (StaticEntity s : staticEntities.values()) {
            if (s instanceof ZombieToastSpawn) {
                ZombieToastSpawn spawner = (ZombieToastSpawn) s;
                // update interactable state
                spawner.update(player);
                spawnZombie(spawner);
            }
        }
    }

    /**
     * Helper function to create a new zombie at relevant ticks
     * @param spawner Zombie spawner to spawn from
     */
    private void spawnZombie(ZombieToastSpawn spawner) {
        if (!(tickCount > 0 && tickCount % gamemode.getSpawnRate() == 0)) {
            return;
        }
        List<Position> possibleSpawnPositions = spawner.spawn();
        Position newPos = getSpawnPosition(possibleSpawnPositions);
        if (newPos == null) {
            // no valid spawn positions
            return;
        }
        Zombie newZombie = new Zombie(newPos.getX(), newPos.getY(), "zombie_toast" + String.valueOf(incrementEntityCount()));
        movingEntities.put(newZombie.getId(), newZombie);
        player.subscribePassiveObserver((PlayerPassiveObserver) newZombie);
    }
    
    /**
     * Get a random spawn position for new zombie
     * @param possibleSpawnPositions List of possible cardinally adjacent positions to a spawner
     * @return position to spawn, or null if no valid positions
     */
    private Position getSpawnPosition(List<Position> possibleSpawnPositions) {
        Position newPos = null;
        Random random = new Random();
        while (!(possibleSpawnPositions.isEmpty())) {
            int posIndex = random.nextInt(possibleSpawnPositions.size());
            newPos = possibleSpawnPositions.get(posIndex);
            if (validZombieSpawnPosition(newPos)) {
                break;
            } else {
                possibleSpawnPositions.remove(posIndex);
            }
            newPos = null;
        }
        return newPos;
    }

    /**
     * Checks whether a given position is a valid zombie spawn position
     * @param position Position to check
     * @return true if the position giveen is valid, otherwise false
     */
    private boolean validZombieSpawnPosition(Position position) {
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
    private int currentSpiders() {
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
            if (!(e instanceof Mercenary)) {
                throw new IllegalArgumentException();
            } else {
                ((Mercenary) e).interact(this);
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
     * Sets the current battle to the provided battle
     * @param battle
     */
    public void setBattle(Battle battle) {
        this.currentBattle = battle;
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

    public int getHighestX() {
        return highestX;
    }

    public int getHighestY() {
        return highestY;
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
        //TODO implement

        int tickNo = gameData.getInt("tick-count");
        int entityNo = gameData.getInt("entity-count");
        
        setEntityCount(entityNo);
        setTickCount(tickNo);

        // TODO can we put this in a shared method
        if (gameData.has("goal-condition")) {
            JSONObject g = gameData.getJSONObject("goal-condition");
            GoalComponent goal = createGoal(g);
            setGoals(goal);
        }
        else {
            setGoals(null);
        }

        JSONArray inventoryItems = gameData.getJSONArray("inventory");
        for (int i = 0; i < inventoryItems.length(); i++) {
            createInventoryFromJSON(inventoryItems.getJSONObject(i));
        }

        JSONObject playerObj = gameData.getJSONObject("player");
        //merc list
        createPlayerFromJSON(playerObj);

        JSONArray movingEntitiesItems = gameData.getJSONArray("moving-entities");
        for (int i = 0; i < movingEntitiesItems.length(); i++) {
            createMovingEntityFromJSON(movingEntitiesItems.getJSONObject(i));
        }

        JSONArray staticEntitiesItems = gameData.getJSONArray("static-entities");
        for (int i = 0; i < staticEntitiesItems.length(); i++) {
            createStaticEntityFromJSON(staticEntitiesItems.getJSONObject(i));
        }

        JSONArray collectableEntitiesItems = gameData.getJSONArray("collectable-entities");
        for (int i = 0; i < collectableEntitiesItems.length(); i++) {
            createCollectableEntityFromJSON(collectableEntitiesItems.getJSONObject(i));
        }

        // trigger any switches with a boulder already on top
        triggerSwitches();

        movingEntities.forEach( (id, me) -> {
            player.subscribePassiveObserver((PlayerPassiveObserver)me);
        });        

        if (gameData.has("current-battle")) {
            JSONObject b = gameData.getJSONObject("current-battle"); 
            currentBattle = new Battle(player, movingEntities.get(b.get("character")), gamemode.isEnemyAttackEnabled());
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
     * Creates inventory from a json
     * @param obj json
     */
    private void createInventoryFromJSON(JSONObject obj) {
        //int x = obj.getInt("x");
        //int y = obj.getInt("y");

        //updateBounds(x, y);

        int durability = obj.getInt("durability");

        String type = obj.getString("type");

        String id = obj.getString("id");

        
        if (type.equals("treasure")) {

            Treasure e = new Treasure(id, durability);
            inventory.collect(e);
        } 
        
        else if (type.equals("key")) {
            int key = obj.getInt("key");
            Key e = new Key(id, key, durability);
            
            inventory.collect(e);

        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(id, durability);

            inventory.collect(e);
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(id, durability, gamemode.isInvincibilityEnabled());
            inventory.collect(e);
        } 
        
        else if (type.equals("invisibility_potion")) {
            InvisibilityPotion e = new InvisibilityPotion(id, durability);
            inventory.collect(e);
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(id, durability);
            inventory.collect(e);
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(id, durability);
            inventory.collect(e);

        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(id, durability);
            inventory.collect(e);
        } 
        
        else if (type.equals("sword")) {
            Sword e = new Sword(id, durability);
            inventory.collect(e);
        }

        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(id, durability);
            inventory.collect(e);
        }

        else if (type.equals("armour")) {
            Armour e = new Armour(id, durability);
            inventory.collect(e);
        }

        else if (type.equals("bow")) {
            Bow e = new Bow(id, durability);
            inventory.collect(e);
        }

        else if (type.equals("shield")) {
            Shield e = new Shield(id, durability);
            inventory.collect(e);
        }
    }

    /**
     * Creates player from a json and remounts observers
     * @param obj json
     */
    private void createPlayerFromJSON(JSONObject obj) {
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String id = obj.getString("id");

        JSONObject healthPoint = obj.getJSONObject("health-point");

        double health = healthPoint.getDouble("health");
        double maxHealth = healthPoint.getDouble("max-health");

        HealthPoint playerHP = new HealthPoint(health, maxHealth);

        if (obj.has("active-potion")) {
            JSONObject activePotion = obj.getJSONObject("active-potion");
            String activePotionType = activePotion.getString("active-potion");
            int activePotionDuration = activePotion.getInt("duration");

            Passive e = null;
            if (activePotionType.equals("invincibility_potion")) {
                e = new InvincibilityPotion(activePotionDuration);
            }
            else if (activePotionType.equals("invisibility_potion")) {
                e = new InvisibilityPotion(activePotionDuration);
            }
            else if (activePotionType.equals("health_potion")) {
                e = new HealthPotion(activePotionDuration);
            }

            Player player = new Player(x, y, id, playerHP, e);
            this.player = player;

        }
        else {
            Player player = new Player(x, y, id, playerHP);
            this.player = player;
        }

    }

    /**
     * Creates moving entity from a json
     * @param obj json
     */
    private void createMovingEntityFromJSON(JSONObject obj) {
        //TODO update constructors

        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String type = obj.getString("type");

        JSONObject movement = obj.getJSONObject("movement");

        String defaultMovement = movement.getString("default-strategy");
        String currentMovement = movement.getString("movement-strategy");

        JSONObject healthPoint = obj.getJSONObject("health-point");

        double health = healthPoint.getDouble("health");
        double maxHealth = healthPoint.getDouble("max-health");

        HealthPoint entityHP = new HealthPoint(health, maxHealth);
        

        String id = obj.getString("id");

        
        if (type.equals("spider")) {
            String currentDir = movement.getString("current-direction");
            String nextDir = movement.getString("next-direction");
            int remMovesCurr = movement.getInt("remMovesCurr");
            int remMovesNext = movement.getInt("remMovesNext");
            boolean avoidPlayer  = movement.getBoolean("avoidPlayer");
            Spider e = new Spider(x, y, id, entityHP, defaultMovement, currentMovement, currentDir, nextDir, remMovesCurr, remMovesNext, avoidPlayer);

            movingEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("zombie_toast")) {
            Zombie e = new Zombie(x, y, id, entityHP, defaultMovement, currentMovement);
            movingEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("mercenary")) {
            boolean isAlly = obj.getBoolean("ally");
            Mercenary e = new Mercenary(x, y, id, entityHP, defaultMovement, currentMovement, isAlly);

            movingEntities.put(e.getId(), e);
        } 
    }

    /**
     * Creates static entity from a json
     * @param obj json
     */
    private void createStaticEntityFromJSON(JSONObject obj) {
        //TODO update constructors
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        updateBounds(x, y);

        String type = obj.getString("type");

        String id = obj.getString("id");

        if (type.equals("wall")) {
            Wall e = new Wall(x, y, id);
            staticEntities.put(e.getId(), e);

        } else if (type.equals("exit")) {
            Exit e = new Exit(x,y,id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("switch")) {
            FloorSwitch e = new FloorSwitch(x, y, id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("boulder")) {
            Boulder e = new Boulder(x, y, id);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("door")) {
            int key = obj.getInt("key");
            boolean opened = obj.getBoolean("open");
            Door e = new Door(x, y, id, key, opened);
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("portal")) {
            Portal e;

            String colour = obj.getString("colour");

            if (staticEntities.containsKey(colour)) {
                e = new Portal(x, y, colour + "2", colour, (Portal) staticEntities.get(colour));
            } else {
                e = new Portal(x, y, colour, colour);
            }
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("zombie_toast_spawner")) {
            ZombieToastSpawn e = new ZombieToastSpawn(x, y, id);
            staticEntities.put(e.getId(), e);   
        } 
        else if (type.equals("bomb")) {
            PlacedBomb e = new PlacedBomb(x, y, id);
            staticEntities.put(e.getId(), e); 
        }
    }

    /**
     * Creates collectible from a json
     * @param obj json
     */
    private void createCollectableEntityFromJSON(JSONObject obj) {
        // TODO update constructors
        int x = obj.getInt("x");
        int y = obj.getInt("y");

        int durability = obj.getInt("durability");

        updateBounds(x, y);

        String type = obj.getString("type");

        String id = obj.getString("id");

        
        if (type.equals("treasure")) {
            Treasure e = new Treasure(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("key")) {
            int key = (int)obj.get("key");
            Key e = new Key(x, y, id, key, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(x, y, id, gamemode.isInvincibilityEnabled());
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invisibility_potion")) {
            int duration = obj.getInt("duration");
            InvisibilityPotion e = new InvisibilityPotion(x, y, id, durability, duration);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("sword")) {
            Sword e = new Sword(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
        }
        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(x, y, id, durability);
            collectableEntities.put(e.getId(), e);
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
                return (SwampTile)se.getMovementFactor();
            }
        }
        return 1.0;
    }
    
}
