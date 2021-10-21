package dungeonmania;

import java.util.List;
import dungeonmania.goal.*;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.character.Battle;
import dungeonmania.character.Player;
import dungeonmania.collectable.CollectableEntities;
import dungeonmania.exceptions.InvalidActionException;

public class World {
    private Gamemode gamemode;
    private Player player;
    private String id;
    private List<List<Goal>> goals;
    //private HashMap<Entity, String> entities; TBC with implementation of overarching Entity class
    private List<CollectableEntities> collectableEntities;
    private List<StaticEntity> staticEntity;
    private List<Character> characters;
    private int entityCount;
    private List<Battle> battles;
    //private Map map; TBC

    /**
     * Constructor for world that takes the string of the dungeon name to build 
     * and a string for the gamemode (Standard, Peaceful, Hard)
     */
    public World(String dugeonName, String gamemode) {
        
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
    public CollectableEntities getCollectableEntity(Position p) {
        return null;
    }

    /**
     * Return the character that exists in the dungeon at position p (if one exists)
     * @param p position to check
     * @return Character at position p
     */
    public Character getCharacter(Position p){
        return null;
    }
}
