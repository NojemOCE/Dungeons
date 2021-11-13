package dungeonmania;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
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

        // For 10 ticks, the mercenary is uncontrolled
        for (int i = 0; i < 10; i++) {
            DungeonResponse d = controller.tick(null, Direction.DOWN);
            for (EntityResponse e : d.getEntities()) {
                if (e.getId().equals("mercenary5")) {
                    assertFalse(e.isInteractable());
                }
            }
        }

        // After 10 ticks, the mercenary is no longer mind controlled
        DungeonResponse notControlled = controller.tick(null, Direction.RIGHT);
        for (EntityResponse e : notControlled.getEntities()) {
            if (e.getId().equals("mercenary5")) {
                assertTrue(e.isInteractable());
            }
        }
    }
}
