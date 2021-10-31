package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class DMControllerTesting {

    /**
     * Test that all combination of games can be created (newGame)
     * - Checks that provided list of expected dungeons can be created with each in built gamemode
     */
    @ParameterizedTest
    @ValueSource(strings = {"advanced", "boulders", "maze"})
    public void testNewGame(String dungeon) {
        DungeonManiaController controller = new DungeonManiaController();
        for (String mode : controller.getGameModes()) {
            assertDoesNotThrow(() -> controller.newGame(dungeon, mode));
        }
    }

    /**
     * Test that an invalid dungeon or gamemode throws an exception when creating new game
     */
    @Test
    public void testInvalidNewGameArguments() {
        DungeonManiaController controller = new DungeonManiaController();

        // test for invalid dungeon
        String validMode = "Standard";
        assertThrows(IllegalArgumentException.class, () -> controller.newGame("does not exist", validMode));

        // test for invalid mode
        String validDungeon = "maze";
        assertThrows(IllegalArgumentException.class, () -> controller.newGame(validDungeon, "does not exist"));

    }


    /**
     * Test that saveGame does not throw and exception and <saves as desired>
     */
    @Test
    public void testSaveGame() {
        DungeonManiaController controller = new DungeonManiaController();

        DungeonResponse newDungeon = controller.newGame("maze", "Standard");

        assertDoesNotThrow(() -> controller.saveGame(newDungeon.getDungeonId()));

    }

    /**
     * Test loadGame works for valid id and throws exception for invalid ID
     */
    // @Test
    // public void testLoadGame() {
    //     DungeonManiaController controller = new DungeonManiaController();

    //     // create a new game and save it
    //     DungeonResponse newDungeon = controller.newGame("maze", "Standard");
    //     String existingId = newDungeon.getDungeonId();
    //     assertDoesNotThrow(() -> controller.saveGame(existingId));

    //     // check that valid id can be loaded
    //     assertDoesNotThrow(() -> controller.loadGame(existingId));
    // }

    /** 
     * Test that all saved games in controller are stored as expected
     */
    @Test
    public void testAllGames() {
        DungeonManiaController controller = new DungeonManiaController();

        List<String> gameIds = new ArrayList<>();

        // create some new games and save them
        for (String mode : controller.getGameModes()) {
            DungeonResponse newDungeon = controller.newGame("maze", mode);
            String currentId = newDungeon.getDungeonId();
            controller.saveGame(currentId);
            gameIds.add(currentId);
        }


        // check that saved games list is as expected
        // assertListAreEqualIgnoringOrder(controller.allGames(), gameIds);
    }


    /**
     * Test the build works as expected
     */
    @Test
    public void testBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d = controller.newGame("buildable-test", "Standard");
        
        // check that exception is thrown if "buildable" is invalid
        assertThrows(IllegalArgumentException.class, () -> controller.build("invalid buildable"));
        
        // check that exception is thrown if the player does not have sufficient items to build
        assertThrows(InvalidActionException.class, () -> controller.build("bow"));
        // collect items for bow
        controller.tick(null, Direction.RIGHT); // wood
        controller.tick(null, Direction.RIGHT); // arrow
        controller.tick(null, Direction.RIGHT); // arrow
        d = controller.tick(null, Direction.RIGHT); // arrow

        try {
            d = controller.build("bow");
        } catch (Exception e) {
            assertTrue(false);
        }

        List<ItemResponse> inventory = d.getInventory();

        boolean bowMade = false;
        for (ItemResponse ir : inventory) {
            if (ir.getType().equals("bow")) {
                bowMade = true;
                break;
            }
        }
        assertTrue(bowMade);

        // now try for shield
        controller.tick(null, Direction.RIGHT); // wood
        controller.tick(null, Direction.RIGHT); // wood
        d = controller.tick(null, Direction.RIGHT); // treasure

        try {
            d = controller.build("shield");
        } catch (Exception e) {
            assertTrue(false);
        }

        inventory = d.getInventory();

        boolean shieldMade = false;
        for (ItemResponse ir : inventory) {
            if (ir.getType().equals("shield")) {
                shieldMade = true;
            }
        }
        assertTrue(shieldMade);
    }

    /**
     * Test that interact works as expected with zombie spawner
     */
    @Test
    public void testInteractZombieSpawner() {
        // check that zombie spawners die when interacted with
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d = controller.newGame("zombieSpawner+sword", "Standard");

        // we are too far away
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assertThrows(InvalidActionException.class, () -> controller.interact(er.getId()));
                break;
            }
        }
        

        // go closer (we have no weapon so should still throw an exception)
        d = controller.tick(null, Direction.RIGHT);
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assertThrows(InvalidActionException.class, () -> controller.interact(er.getId()));
                break;
            }
        }

        // go pick up the sword
        d = controller.tick(null, Direction.DOWN);
        d = controller.tick(null, Direction.RIGHT);
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assertDoesNotThrow(() -> controller.interact(er.getId()));
                break;
            }
        }
        // tick once and check that it is destroyed
        d = controller.tick(null, Direction.UP);
        boolean destroyed = true;
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("zombie_toast_spawner")) {
                destroyed = false;
                break;
            }
        }
        assertTrue(destroyed);        
    }


    /**
     * Test that zombie spawner works as expected
     */
    @Test
    public void testZombieSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d = controller.newGame("zombieSpawner+sword", "Standard");

        // test zombie spawn rate
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                d = controller.tick(null, Direction.DOWN);
            } else {
                d = controller.tick(null, Direction.UP);
            }
            // make sure no zombies spawn in this time
            for (EntityResponse er : d.getEntities()) {
                assertFalse(er.getType().equals("zombie_toast"));
            }
        }

        d = controller.tick(null, Direction.UP);
        // make sure a zombie has spawned (and only one)
        int numZombies = 0;
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("zombie_toast")) {
                numZombies++;
            }
        }
        assert(numZombies == 1);

    }
}
