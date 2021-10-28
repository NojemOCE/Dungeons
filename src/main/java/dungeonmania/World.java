package dungeonmania;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import dungeonmania.buildable.*;
import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;



// TODO: remember to implement all the observer interfaces as we go
public class World {

    private int width;
    private int height;

    private Inventory inventory;
    private Gamemode gamemode;
    private Player player;
    private String id; //TODO only ever set if game is saved?
    private GoalComponent goals;
    //private HashMap<String, Entity> entities; TBC with implementation of overarching Entity class
    private Map<String, CollectableEntity> collectableEntities;
    private Map<String, StaticEntity> staticEntities; // Map<entityId, EntityType>
    private Map<String, MovingEntity> movingEntities;
    private int entityCount;
    private Battle currentBattle;
    private String dungeonName;

    private int tickCount = 0;
    //private Map map; TBC

    /**
     * Constructor for world that takes the string of the dungeon name to build 
     * and a string for the gamemode (Standard, Peaceful, Hard)
     */
    public World(String dungeonName, String gameMode) {
        this.dungeonName = dungeonName;
        this.entityCount = 0;
        if (gameMode.equals("Hard")) {
            this.gamemode = new Hard();
        } else if (gameMode.equals("Standard")) {
            this.gamemode = new Standard();
        } else {
            this.gamemode = new Peaceful();
        }
    }

    /**
     * Create the entities from the world template
     * @param worldData JSON object containing world template
     * @return world dungeon reponse
     */
    public DungeonResponse buildWorld(JSONObject worldData) {
        String width = worldData.getString("width");
        String height = worldData.getString("height");

        setHeight((Integer.parseInt(height)));
        setWidth((Integer.parseInt(width)));

        JSONArray entities = worldData.getJSONArray("entities");

        for (int i = 0; i < entities.length(); i++) {
            createEntity(entities.getJSONObject(i), String.valueOf(incrementEntityCount()));
        }


        JSONObject goals = worldData.getJSONObject("goal-condition");
        GoalComponent goal = createGoal(goals);
        setGoals(goal);

        return worldDungeonResponse();
    }

    

    private void createEntity(JSONObject obj, String id) {
        int x = Integer.parseInt(obj.getString("x"));
        int y = Integer.parseInt(obj.getString("y"));

        String type = obj.getString("type");

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
            String key = obj.getString("key");
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
            movingEntities.put(e.getId(), e);
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
            Treasure e = new Treasure(new Position(x,y), id, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("key")) {
            String key = obj.getString("key");
            Key e = new Key(new Position(x,y), id, inventory, key);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("health_potion")) {
            HealthPotion e = new HealthPotion(new Position(x,y), id, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invincibility_potion")) {
            InvincibilityPotion e = new InvincibilityPotion(new Position(x,y), id, this, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("invisibility_potion")) {
            InvisibilityPotion e = new InvisibilityPotion(new Position(x,y), id, this, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("wood")) {
            Wood e = new Wood(new Position(x,y), id, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("arrow")) {
            Arrows e = new Arrows(new Position(x,y), id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("bomb")) {
            Bomb e = new Bomb(new Position(x,y), id, this, inventory);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("sword")) {
            Sword e = new Sword(new Position(x,y), id, inventory);
            collectableEntities.put(e.getId(), e);
        }
    }

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

    private String getGoalsResponse() {
        return goals.remainingGoalString();
    }
    private void setGoals(GoalComponent goals) {
        this.goals = goals;
    }


    // tick for all characters and then return world dungeon response
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if itemUsed is not a bomb, invincibility_potion or an invisibility_potion
        // InvalidActionException if itemUsed is not in the player's inventory
        
        if (!Objects.isNull(currentBattle)) {
            currentBattle.battleTick(inventory);
            if (!currentBattle.isActiveBattle()) {
                if (currentBattle.getPlayerWins()) {
                    // return a dropped item
                    movingEntities.remove(currentBattle.getCharacter().getId());
                } else {
                    this.player = null; // will end game in dungeon response
                    // needs to return early
                }
            }
        } else  {
            player.tick(itemUsed, movementDirection, this);
        }
        if (itemUsed.isEmpty()) {
            inventory.tick(movementDirection);
        } else {
            inventory.tick(itemUsed);
        }

        // now move all entities
        for (MovingEntity me: movingEntities.values()) {
            me.move(this);
            if (me.getPosition().equals(player.getPosition())) {
                currentBattle = player.battle(me, inventory); // if invisible it will add null
                player.notifyObservers(1);
            }
        }

        // Now evaluate goals. Goal should never be null, but add a check incase there is an error in the input file
        if (!goals.equals(null)){
            goals.evaluate(this);
        }
        

        return worldDungeonResponse();
    }


    // Interacts with a mercenary (where the character bribes the mercenary) or a zombie spawner (where the character destroys the zombie spawner)
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
                // TODO??
                ((Mercenary) e).interact();
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

    // Builds the given entity where buildable is one of "bow" or "shield"
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if buildable is not one of bow or shield
        // InvalidActionException if the player does not have sufficient items to craft the buildable
        return null;
    }

    // Return a dungeon response for the current world
    public DungeonResponse worldDungeonResponse(){

        return new DungeonResponse(id, dungeonName, getEntityResponses(), getInventoryResponse(), inventory.getBuildable(), getGoalsResponse());
    }

    /**
     * Returns the static entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Static entity at position p
     */
    public StaticEntity getStaticEntity(Position p) {
        for (StaticEntity s: staticEntities.values()) {
            if (s.getPosition().equals(p))  {
                return s;
            }
        }
        return null;
    }


    // An alternative method of the above method (getStaticEntity). I think this
    // might be the better choice as it allows us to consider layers
    // eg. if a boulder is already on top of a switch
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
     * Checks if a given Buildable item is in the players inventory
     * @param item buildable item to check for in inventory
     * @return true if the item is in the players inventory, false otherwise
     */
    public boolean inInventory(Buildable item) {
        return inventory.inInventory(item.getItemId());
    }

    public Key keyInInventory(String keyColour) {
        return inventory.keyInInventory(keyColour);
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int incrementEntityCount() {
        this.entityCount++;
        return this.entityCount;
    }


    public List<EntityResponse> getEntityResponses() {
        List<EntityResponse> entityResponses = new ArrayList<>();
        
        entityResponses.add(player.getEntityResponse());
        entityResponses.addAll(movingEntities.values().stream().map(MovingEntity::getEntityResponse).collect(Collectors.toList()));
        entityResponses.addAll(staticEntities.values().stream().map(StaticEntity::getEntityResponse).collect(Collectors.toList()));
        entityResponses.addAll(collectableEntities.values().stream().map(CollectableEntity::getEntityResponse).collect(Collectors.toList()));
        
        return entityResponses;
    }

    public List<ItemResponse> getInventoryResponse(){
        return inventory.getInventoryResponse();
    }

    public boolean inBounds(int x, int y) {
        return !(x < 0 || x >= width || y < 0 || y >= height);
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

    public String saveGame() {
        JSONObject saveGame = new JSONObject();
        saveGame.put(key, value);
    }
}
