package dungeonmania;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventory.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class sceptreTest {

    @Test
    public void testCraftSpectre() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spectreWorld", "standard");
        // Check that we can't craft spectre

        assertThrows(InvalidActionException.class, () -> controller.build("spectre"));
        // Walk around and collect everything
        // Try craft Spectre
        controller.build("spectre");
    }

    @Test
    public void testMindControl() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spectreWorld", "standard");
        // Craft spectre
        // Bribe mercenary
        // Bribe assassin
        // Make sure they are controlled
        // Tick 10 times
        // Make sure they are both not controlled
    }
}
