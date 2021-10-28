package dungeonmania;

import java.util.ArrayList;
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
public class World implements ObserverExitGoal {

    private int width;
    private int height;

    private Inventory inventory;
    private Gamemode gamemode;
    private Player player;
    private String id;
    private List<List<Goal>> goals; // TODO Composite pattern
    //private HashMap<String, Entity> entities; TBC with implementation of overarching Entity class
    private Map<String, CollectableEntity> collectableEntities;
    private Map<String, StaticEntity> staticEntities; // Map<entityId, EntityType>
    private Map<String, MovingEntity> movingEntities;
    private int entityCount;
    private Battle currentBattle;
    private String goalString;
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
        if (gamemode.equals("Hard")) {
            this.gamemode = new Hard();
        } else if (gamemode.equals("Standard")) {
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
            String x = entities.getJSONObject(i).getString("x");
            String y = entities.getJSONObject(i).getString("y");
            String type = entities.getJSONObject(i).getString("type");
            createEntity(entities.getJSONObject(i), String.valueOf(incrementEntityCount()));
        }
        // read goals

        // TODO: look into composite pattern and try to implement that

        // JSONObject goals = worldData.getJSONObject("goal-condition");
        // String goalCondition = goals.getString("goal");
        // JSONArray subgoals = goals.getJSONArray("subgoals");
        // for (int i = 0; i < subgoals.length(); i++) {
        //     String goal = subgoals.getJSONObject(i).getString("goal");
        // }​

        return worldDungeonResponse();
    }

    // private void getGoals() {

    // }
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
                e = new Portal(x, y, colour + "2", (Portal) staticEntities.get(colour));
            } else {
                e = new Portal(x, y, colour);
            }
            staticEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("zombie_toast_spawner")) {
            ZombieToastSpawn e = new ZombieToastSpawn(x, y, id, gamemode.getSpawnRate());
            staticEntities.put(e.getId(), e);   
        } else if (type.equals("player")) {
            Player e = new Player(x, y, id);
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
        
        /*else if (type.equals("armour")) {
            Armour e = new Armour(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("one_ring")) {
            OneRing e = new OneRing(x, y, id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("bow")) {
            Bow e = new Bow(id);
            collectableEntities.put(e.getId(), e);
        } 
        
        else if (type.equals("shield")) {
            Shield e = new Shield(id);
            collectableEntities.put(e.getId(), e);
        }*/
    }


    // tick for all characters and then return world dungeon response
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if itemUsed is not a bomb, invincibility_potion or an invisibility_potion
        // InvalidActionException if itemUsed is not in the player's inventory
        
        if (!Objects.isNull(currentBattle)) {
            currentBattle.battleTick();
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

        }

        if (staticEntities.containsKey(entityId)) {

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

        List<String> buildableList = new ArrayList<>();
        // Here we would need to add a list of all current buildable items
        // ie. if shield is buildable add "shield" to list
        // if bow is buildable add "bow" to list

        // TODO implement real buildable list
        // TODO implement real goals list
        buildableList.add("not a real list of buildable strings");

        return new DungeonResponse(id, dungeonName, getEntityResponses(), getInventoryResponse(), buildableList, "not real goals");
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

    @Override
    public void update(SubjectExitGoal obj) {
        // TODO Auto-generated method stub
        
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


    // Remove all entities from the given positions (except the player)
    public void detonate(Position position) {
        
        List<CollectableEntity> toRemove = new ArrayList<>();
        for (CollectableEntity e : collectableEntities.values()) {
            if (e.getPosition().equals(position)) {
                toRemove.add(e);
            }
        }
        for (CollectableEntity e : toRemove) {
            collectableEntities.remove(e.getId());
        }

        List<StaticEntity> toRemoveStatic = new ArrayList<>();
        for (StaticEntity e : staticEntities.values()) {
            if (e.getPosition().equals(position)) {
                toRemoveStatic.add(e);
            }
        }
        for (StaticEntity e : toRemoveStatic) {
            staticEntities.remove(e.getId());
        }

        List<MovingEntity> toRemoveMoving = new ArrayList<>();
        for (MovingEntity e : movingEntities.values()) {
            if (e instanceof Player) {
                // don't remove
            } else if (e.getPosition().equals(position)) {
                toRemoveMoving.add(e);
            }
        }
        for (MovingEntity e : toRemoveMoving) {
            movingEntities.remove(e.getId());
        }

    }
}
