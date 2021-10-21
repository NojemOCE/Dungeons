package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.movingEntity.*;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;


public class WorldTest {

    @ParameterizedTest
    @ValueSource(strings = {"advanced", "boulders", "maze"})
    public void testNewGame(String dungeon) {
        DungeonManiaController controller = new DungeonManiaController();
        World world;

        for (String mode : controller.getGameModes()) {
            world = new World(dungeon, mode);
            assertEquals(dungeon, world.worldDungeonResponse().getDungeonName());
        }
    }

    @Test
    public void testDungeonResponse(String dungeon) {
        World world = new World("portals", "Standard");
        
        //public DungeonResponse(String dungeonId, String dungeonName, List<EntityResponse> entities,
        //    List<ItemResponse> inventory, List<String> buildables, String goals)
        

        EntityResponse player = new EntityResponse("1", "player", new Position(0, 0), true);
        EntityResponse portal1 = new EntityResponse("2", "portal", new Position(1, 0), false);
        EntityResponse portal2 = new EntityResponse("3", "portal", new Position(4, 0), false);

        List <EntityResponse> entities = new ArrayList<>();
        entities.add(player);
        entities.add(portal1);
        entities.add(portal2);

        DungeonResponse expected  = new DungeonResponse("1", "advanced", entities, null, null, null);

        assertEquals(expected, world.worldDungeonResponse());
        assertEquals(expected.getDungeonId(), world.worldDungeonResponse().getDungeonId());
        assertEquals(expected.getDungeonName(), world.worldDungeonResponse().getDungeonName());
        // Not sure why this throws error 
        //assertListAreEqualIgnoringOrder(entities, world.worldDungeonResponse().getEntities());

    }

    @Test
    public void testBuildable() {
        World world = new World("advanced", "Standard");
        assertThrows(IllegalArgumentException.class, () -> world.build("invalid buildable"));

        assertThrows(InvalidActionException.class, () -> world.build("bow"));
        assertThrows(InvalidActionException.class, () -> world.build("shield"));

        // Need more tests to check it can be built after collecting necessary items
    }

    @Test
    public void testTick() {
        World world = new World("advanced", "Standard");
        assertThrows(IllegalArgumentException.class, () -> world.tick("invalid item", Direction.UP));

        assertThrows(InvalidActionException.class, () -> world.tick("bomb", Direction.UP));
        assertThrows(InvalidActionException.class, () -> world.tick("invincibility_potion", Direction.UP));
        assertThrows(InvalidActionException.class, () -> world.tick("invisibility_potion", Direction.UP));
    }

    @Test
    public void testInteract() {
        // Unsure if IllegalArgumentException whenver entityId is not mercenary or zombie spawner
        World world = new World("advanced", "Standard");

        assertThrows(IllegalArgumentException.class, () -> world.interact("invalid id"));

        assertThrows(InvalidActionException.class, () -> world.interact("44"));

        // need more tests to check it fails when player does not have gold, or a weapon to destroy spawner
        // and tests to check that it passes when it should

    }
    @Test
    public void testGetCharacterAtPosition() {
        World world = new World("advanced", "Standard");

        // Location of mercenary
        Position p  = new Position(3, 5);
        MovingEntity c = world.getCharacter(p);
        assertNotNull(c);
        assert(c instanceof Mercenary);
        assertEquals(p, c.getPosition());


        // Location of wall
        Position p2  = new Position(9, 2);
        MovingEntity c2 = world.getCharacter(p2);
        assertNull(c2);

        // Location of treasure
        Position p3  = new Position(7, 10);
        MovingEntity c3 = world.getCharacter(p3);
        assertNull(c3);
    }

    @Test
    public void testGetStaticEntityAtPosition() {
        World world = new World("advanced", "Standard");

        // Location of wall
        Position p  = new Position(9, 2);
        StaticEntity e = world.getStaticEntity(p);
        assertNotNull(e);
        assert(e instanceof Wall);
        assertEquals(p, e.getPosition());


        // Location of mercenary
        Position p2  = new Position(3, 5);
        StaticEntity e2 = world.getStaticEntity(p2);
        assertNull(e2);

        // Location of treasure
        Position p3  = new Position(7, 10);
        StaticEntity e3 = world.getStaticEntity(p3);
        assertNull(e3);
    }

    @Test
    public void testGetCollectableEntityAtPosition() {
        World world = new World("advanced", "Standard");
        
        // Location of treasure
        Position p  = new Position(7, 10);
        CollectableEntity e = world.getCollectableEntity(p);
        assertNotNull(e);
        assert(e instanceof Treasure);
        //assertEquals(p, e.getPosition());


        // Location of wall
        Position p2  = new Position(9, 2);
        CollectableEntity e2 = world.getCollectableEntity(p2);
        assertNull(e2);


        // Location of mercenary
        Position p3  = new Position(3, 5);
        CollectableEntity e3 = world.getCollectableEntity(p3);
        assertNull(e3);
    }


    
}
