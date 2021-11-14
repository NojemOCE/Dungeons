package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

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
    public void testAltRecipe() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreWorld", "standard");

        // Check that we can't craft sceptre
        assertThrows(InvalidActionException.class, () -> controller.build("sceptre"));

        // Walk around and collect everything
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        // Try craft Sceptre
        assertDoesNotThrow(() -> controller.build("sceptre"));
    }

    @Test
    public void saveSceptre() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreWorld", "standard");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        assertDoesNotThrow(() -> controller.build("sceptre"));
        assertDoesNotThrow(() -> controller.interact("mercenary5"));

        // Walk down for 2 ticks and save
        IntStream.range(0, 2).forEach(i -> controller.tick(null, Direction.DOWN));
        controller.saveGame("saveSceptreWorld");

        // Load game
        controller.loadGame("saveSceptreWorld");

        // Walk down for 8 ticks, mercenary should still be controlled
        for (int i = 0; i < 8; i++) {
            DungeonResponse d = controller.tick(null, Direction.DOWN);
            for (EntityResponse e : d.getEntities()) {
                if (e.getId().equals("mercenary5")) {
                    assertFalse(e.isInteractable());
                }
            }
        }

        // After 10 ticks, the mercenary is no longer mind controlled
        DungeonResponse notControlled = controller.tick(null, Direction.DOWN);
        for (EntityResponse e : notControlled.getEntities()) {
            if (e.getId().equals("mercenary5")) {
                assertTrue(e.isInteractable());
            }
        }
    }

    @Test
    public void testMindControl() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreWorld", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("sceptre"));

        // Mind control the mercenary
        assertDoesNotThrow(() -> controller.interact("mercenary5"));

        // For 10 ticks, the mercenary is uncontrolled
        for (int i = 0; i < 10; i++) {
            DungeonResponse d = controller.tick(null, Direction.DOWN);
            for (EntityResponse e : d.getEntities()) {
                if (e.getId().equals("mercenary5")) {
                    //assertFalse(e.isInteractable());
                    // check that we can't interact with merc
                    assertThrows(InvalidActionException.class, () -> controller.interact("mercenary5"));
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

    @Test
    public void testCooldown() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreMindControl", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("sceptre"));

        // Mind control the mercenary
        assertDoesNotThrow(() -> controller.interact("mercenary5"));

        // Move down for 3 ticks
        for (int i = 0; i < 3; i++) {
            DungeonResponse d = controller.tick(null, Direction.DOWN);
            for (EntityResponse e : d.getEntities()) {
                if (e.getId().equals("mercenary5")) {
                    assertFalse(e.isInteractable());
                }
                assertThrows(InvalidActionException.class, () -> controller.interact("mercenary6"));
            }
        }

        // Collect coin
        controller.tick(null, Direction.DOWN);
        assertDoesNotThrow(() -> controller.interact("mercenary6"));

        // Move down for 6 ticks
        for (int i = 0; i < 6; i++) {
            DungeonResponse d = controller.tick(null, Direction.DOWN);
            for (EntityResponse e : d.getEntities()) {
                if (e.getId().equals("mercenary5") || e.getId().equals("mercenary6")) {
                    assertFalse(e.isInteractable());
                }
            }
        }

        // After 10 ticks, the mercenary is no longer mind controlled
        // The mercenary that is bribed should still be an ally
        DungeonResponse notControlled = controller.tick(null, Direction.DOWN);
        for (EntityResponse e : notControlled.getEntities()) {
            if (e.getId().equals("mercenary5")) {
                assertTrue(e.isInteractable());
            } else if (e.getId().equals("mercenary6")) {
                assertFalse(e.isInteractable());
            }
        }

        // Cooldown of sceptre should be on 4 ticks
        // Move down 3 more times - during this, we shouldn't be able to mind control the mercenary
        for (int i = 0; i < 3; i++) {
            controller.tick(null, Direction.DOWN);
            assertThrows(InvalidActionException.class, () -> controller.interact("mercenary5"));
        }

        // After the last tick, the sceptre should be off cooldown
        controller.tick(null, Direction.DOWN);
        assertDoesNotThrow(() -> controller.interact("mercenary5"));
    }

    @Test
    public void testPriority() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("sceptreMindControl", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertDoesNotThrow(() -> controller.build("sceptre"));

        // Move down for 3 ticks
        IntStream.range(0, 3).forEach(i -> controller.tick(null, Direction.DOWN));

        // Collect coin
        controller.tick(null, Direction.DOWN);

        // Assert that sceptre has priority over bribing
        assertDoesNotThrow(() -> controller.interact("mercenary5"));

        // Coin should still be in the inventory
        DungeonResponse hasTreasure = controller.tick(null, Direction.DOWN);
        assertTrue(hasTreasure.getInventory().stream().anyMatch(i -> i.getType().equals("treasure")));
    }
}