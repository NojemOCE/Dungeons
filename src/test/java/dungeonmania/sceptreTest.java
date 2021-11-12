package dungeonmania;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventory.Inventory;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class sceptreTest {

    @Test
    public void testCraftSceptre() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreWorld", "standard");

        // Check that we can't craft sceptre
        assertThrows(InvalidActionException.class, () -> controller.build("sceptre"));

        // Walk around and collect everything
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        // Try craft Sceptre
        assertDoesNotThrow(() -> controller.build("sceptre"));
    }

    @Test
    public void testMindControl() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreWorld", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.build("sceptre");

        // Mind control the mercenary
        assertDoesNotThrow(() -> controller.interact("mercenary5"));
        assertDoesNotThrow(() -> controller.interact("mercenary6"));
        // Craft sceptre
        // Bribe mercenary
        // Bribe assassin
        // Make sure they are controlled
        // Tick 10 times
        // Make sure they are both not controlled
    }
}
