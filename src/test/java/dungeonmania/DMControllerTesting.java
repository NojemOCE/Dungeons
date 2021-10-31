package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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


    // TODO: update this with how the save should be made (check that it writes to correct spot)
    /**
     * Test that saveGame does not throw and exception and <saves as desired>
     */
    @Test
    public void testSaveGame() {
        DungeonManiaController controller = new DungeonManiaController();

        DungeonResponse newDungeon = controller.newGame("maze", "Standard");

        assertDoesNotThrow(() -> controller.saveGame(newDungeon.getDungeonId()));

        // TODO: check that save actually occurred
    }

    /**
     * Test loadGame works for valid id and throws exception for invalid ID
     */
    @Test
    public void testLoadGame() {
        DungeonManiaController controller = new DungeonManiaController();

        // create a new game and save it
        DungeonResponse newDungeon = controller.newGame("maze", "Standard");
        String existingId = newDungeon.getDungeonId();
        assertDoesNotThrow(() -> controller.saveGame(existingId));

        // check that valid id can be loaded
        assertDoesNotThrow(() -> controller.loadGame(existingId));

        // check that invalid id will throw an exception
        String madeUpId = existingId + "randomStuff";
        assertThrows(IllegalArgumentException.class, () -> controller.saveGame(madeUpId));
    }

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
        assertListAreEqualIgnoringOrder(controller.allGames(), gameIds);
    }

    /**
     * Test the ticks operate as expected
     * (Probably need to split this up)
     */
    @Test
    public void testTick() {
        // create a game
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse newDungeon = controller.newGame("maze", "Standard");


        // TODO:
        // test that basic movement works

        // test that we can't walk into walls

        // wooowww there is a lot more to test...pretty much all possible interactions??

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
     * Test that interact works as expected
     */
    @Test
    public void testInteract() {
        // check that mercenaries become allies when bribed


        // check that zombie spawners die when interacted with
    }
}
