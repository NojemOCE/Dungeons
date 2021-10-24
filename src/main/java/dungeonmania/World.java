package dungeonmania;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.goal.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.gamemode.*;
import dungeonmania.movingEntity.*;
import dungeonmania.buildable.Buildable;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.exceptions.InvalidActionException;


// TODO: remember to implement all the observer interfaces as we go
public class World implements ObserverExitGoal {

    private int width;
    private int height;


    private Gamemode gamemode;
    private Player player;
    private String id;
    private List<List<Goal>> goals; // TODO Composite pattern
    //private HashMap<String, Entity> entities; TBC with implementation of overarching Entity class
    private Map<String, CollectableEntity> collectableEntities;
    private Map<String, StaticEntity> staticEntities; // Map<entityId, EntityType>
    private Map<String, MovingEntity> movingEntities;
    private int entityCount;
    private List<Battle> battles;
    private String goalString;
    private String dungeonName;
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
        
    }

    private void createEntity(JSONObject obj, String id) {
        int x = Integer.parseInt(obj.getString("x"));
        int y = Integer.parseInt(obj.getString("y"));

        String type = obj.getString("type");


        if (type.equals("wall")) {
            Wall e = new Wall(x,y,id);
            staticEntities.put(e.getId(), e);
        } else if (type.equals("exit")) {
            Exit e = new Exit(x,y,id);
            staticEntities.put(e.getId(), e);
        } else if (type.equals("switch")) {
            FloorSwitch e = new FloorSwitch(x, y, id);
            staticEntities.put(e.getId(), e);
        } else if (type.equals("boulder")) {
            Boulder e = new Boulder(x, y, id);
            staticEntities.put(e.getId(), e);
        } else if (type.equals("door")) {
            String key = obj.getString("key");
            Door e = new Door(x, y, id, key);
            staticEntities.put(e.getId(), e);
        } else if (type.equals("portal")) {
            Portal e;
            String colour = obj.getString("colour");
            if (staticEntities.containsKey(colour)) {
                e = new Portal(x, y, colour + "2", (Portal) staticEntities.get(colour));
            } else {
                e = new Portal(x, y , colour);
            }
            staticEntities.put(e.getId(), e);
        } else if (type.equals("zombie_toast_spawner")) {
            ZombieToastSpawn e = new ZombieToastSpawn(x, y, id);
            staticEntities.put(e.getId(), e);   
        } else if (type.equals("player")) {
            Player e = new Player(x, y ,id)
            this.player = e;
            movingEntities.put(e.getId(), e);
        } else if (type.equals("spider")) {
            Spider e = new Spider(x, y, id);
        } else if (type.equals("zombie")) {
            Zombie e = new Zombie(x, y, id);
            movingEntities.put(e.getId(), e);
        } else if (type.equals("mercenary")) {
            Mercenary e = new Mercenary(x, y, id);
            movingEntities.put(e.getId(), e);
        }

    }

    // tick for all characters and then return world dungeon response
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if itemUsed is not a bomb, invincibility_potion or an invisibility_potion
        // InvalidActionException if itemUsed is not in the player's inventory

        return null;
    }


    // Interacts with a mercenary (where the character bribes the mercenary) or a zombie spawner (where the character destroys the zombie spawner)
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if entityId is not a valid entityId
        // InvalidAction Exception if:
        // * the player is not cardinally adjacent to the given entity,
        // * the player does not have any gold and attempts to bribe a mercenary
        // * the player does not have a weapon and attempts to destroy a spawner
        if (characters.containsKey(key)) {

        }

        if (staticEntity.containsKey(key)) {

        }

        return null;
    }

    // Builds the given entity where buildable is one of "bow" or "shield"
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // IllegalArgumentException if buildable is not one of bow or shield
        // InvalidActionException if the player does not have sufficient items to craft the buildable
        return null;
    }

    // Return a dungeon response for the current world
    public DungeonResponse worldDungeonResponse(){
        return null;
    }

    /**
     * Returns the static entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Static entity at position p
     */
    public StaticEntity getStaticEntity(Position p) {
        return null;
    }

    /**
     * Return sthe Collectable entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Collectable entity at position p
     */
    public CollectableEntity getCollectableEntity(Position p) {
        return null;
    }

    /**
     * Return the character that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return MovingEntity at position p
     */
    public MovingEntity getCharacter(Position p){
        return null;
    }

    public List<Battle> getBattles() {
        return null;
    }

    public Player getPlayer() {
        return null;
    }

    public boolean inInventory(CollectableEntity item) {
        return player.inInventory(item);
    }

    public boolean inInventory(Buildable item) {
        return player.inInventory(item);
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
}
