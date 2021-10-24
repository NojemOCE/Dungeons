package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.goal.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.movingEntity.*;
import dungeonmania.buildable.Buildable;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.EntityResponse;


// TODO: remember to implement all the observer interfaces as we go
public class World implements ObserverExitGoal {
    private Gamemode gamemode;
    private Player player;
    private String id;
    private List<List<Goal>> goals;
    //private HashMap<Entity, String> entities; TBC with implementation of overarching Entity class
    private List<CollectableEntity> collectableEntities;
    private List<StaticEntity> staticEntities;
    private List<MovingEntity> characters;
    private int entityCount;
    private List<Battle> battles;
    private String goalString;
    private String dungeonName;
    //private Map map; TBC

    /**
     * Constructor for world that takes the string of the dungeon name to build 
     * and a string for the gamemode (Standard, Peaceful, Hard)
     */
    public World(String dungeonName, String gamemode) {
        
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

        List<String> buildableList = new ArrayList<>();
        // Here we would need to add a list of all current buildable items
        // ie. if shield is buildable add "shield" to list
        // if bow is buildable add "bow" to list
        buildableList.add("not a real list of buildable strings");

        return new DungeonResponse(id, dungeonName, getEntityResponses(), getInventoryResponse(), buildableList, "not real goals");
    }

    /**
     * Returns the static entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Static entity at position p
     */
    public StaticEntity getStaticEntity(Position p) {
        for (StaticEntity s: staticEntities) {
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
        return staticEntities.stream().filter(x  -> x.getPosition().equals(p)).collect(Collectors.toList());
    }

    /**
     * Return sthe Collectable entity that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Collectable entity at position p
     */
    public CollectableEntity getCollectableEntity(Position p) {
        // I am not very good at streams, so not sure if there is an elegant way to use them to return only 1 object from a list
        for (CollectableEntity e: collectableEntities) {
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
        for (MovingEntity c: characters) {
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
    public List<Battle> getBattles() {
        return battles;
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
        return player.inInventory(item);
    }

    /**
     * Checks if a given Buildable item is in the players inventory
     * @param item buildable item to check for in inventory
     * @return true if the item is in the players inventory, false otherwise
     */
    public boolean inInventory(Buildable item) {
        return player.inInventory(item);
    }

    @Override
    public void update(SubjectExitGoal obj) {
        // TODO Auto-generated method stub
        
    }

    public List<EntityResponse> getEntityResponses() {
        List<EntityResponse> entityResponses = new ArrayList<>();
        
        entityResponses.add(player.getEntityResponse());
        entityResponses.addAll(characters.stream().map(MovingEntity::getEntityResponse).collect(Collectors.toList()));
        entityResponses.addAll(staticEntities.stream().map(StaticEntity::getEntityResponse).collect(Collectors.toList()));
        entityResponses.addAll(collectableEntities.stream().map(CollectableEntity::getEntityResponse).collect(Collectors.toList()));
        
        return entityResponses;
    }

    public List<ItemResponse> getInventoryResponse(){
        return player.getInventoryResponse();
    }
}
