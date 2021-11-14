package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;


import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryControllerTesting {

    /**
     * When collecting an item, an item should appear in the inventory
     */
    @Test
    public void testCollectItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "standard");

        // Collect health potion
        DungeonResponse collectHealth = controller.tick(null, Direction.DOWN);
        assertTrue(collectHealth.getInventory().stream().anyMatch(ir -> ir.getType().equals("health_potion")));

        // Collect invincibility potion
        DungeonResponse collectInvincible = controller.tick(null, Direction.RIGHT);
        assertTrue(collectInvincible.getInventory().stream().anyMatch(ir -> ir.getType().equals("invincibility_potion")));

        // Collect invisibility potion
        DungeonResponse collectInvisible = controller.tick(null, Direction.RIGHT);
        assertTrue(collectInvisible.getInventory().stream().anyMatch(ir -> ir.getType().equals("invisibility_potion")));

        // Collect wood
        DungeonResponse collectWood = controller.tick(null, Direction.RIGHT);
        assertTrue(collectWood.getInventory().stream().anyMatch(ir -> ir.getType().equals("wood")));
    }

    /**
     * Test consuming potions
     */
    @Test
    public void testTakePotion() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "standard");

        // Collect health potion
        DungeonResponse collectHealth = controller.tick(null, Direction.DOWN);
        assertTrue(collectHealth.getInventory().stream().anyMatch(ir -> ir.getType().equals("health_potion")));

        // Collect invincibility potion
        DungeonResponse collectInvincible = controller.tick(null, Direction.RIGHT);
        assertTrue(collectInvincible.getInventory().stream().anyMatch(ir -> ir.getType().equals("invincibility_potion")));

        // Collect invisibility potion
        DungeonResponse collectInvis = controller.tick(null, Direction.RIGHT);
        assertTrue(collectInvis.getInventory().stream().anyMatch(ir -> ir.getType().equals("invisibility_potion")));

        // Take health potion
        DungeonResponse takePotion = controller.tick("health_potion5", Direction.NONE);
        assertFalse(takePotion.getInventory().stream().anyMatch(ir -> ir.getType().equals("health_potion")));

        // Take invincibility potion
        DungeonResponse takeInvincibility = controller.tick("invincibility_potion6", Direction.NONE);
        assertFalse(takeInvincibility.getInventory().stream().anyMatch(ir -> ir.getType().equals("invincibility_potion")));


        // Take invincibility potion
        DungeonResponse takeInvisibility = controller.tick("invisibility_potion7", Direction.NONE);
        assertFalse(takeInvisibility.getInventory().stream().anyMatch(ir -> ir.getType().equals("invisibility_potion")));
    }

    /**
     * Test the building of items (shield and bow)
     */
    @Test
    public void testBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("building-world", "standard");

        assertThrows(InvalidActionException.class, () -> controller.build("shield"));
        assertThrows(InvalidActionException.class, () -> controller.build("bow"));

        // Collect crafting materials for shield and bow
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        DungeonResponse checkInventory = controller.tick(null, Direction.LEFT);
        assertTrue(checkInventory.getInventory().stream().anyMatch(ir -> ir.getType().equals("wood")));
        assertTrue(checkInventory.getInventory().stream().anyMatch(ir -> ir.getType().equals("treasure")));
        assertTrue(checkInventory.getInventory().stream().anyMatch(ir -> ir.getType().equals("arrow")));

        assertDoesNotThrow(() -> controller.build("shield"));
        assertDoesNotThrow(() -> controller.build("bow"));

        DungeonResponse checkCrafted = controller.tick(null, Direction.LEFT);
        assertFalse(checkCrafted.getInventory().stream().anyMatch(ir -> ir.getType().equals("wood")));
        assertFalse(checkCrafted.getInventory().stream().anyMatch(ir -> ir.getType().equals("treasure")));
        assertFalse(checkCrafted.getInventory().stream().anyMatch(ir -> ir.getType().equals("arrow")));
        assertTrue(checkCrafted.getInventory().stream().anyMatch(ir -> ir.getType().equals("shield")));
        assertTrue(checkCrafted.getInventory().stream().anyMatch(ir -> ir.getType().equals("bow")));

    }

    /**
     * Test consuming invincibility potion
     */
    @Test
    public void testTickInvincibility() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "standard");

        // Collect invincibility potion
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);

        // Test saving and loading
        controller.saveGame("saveCollectedInvincibility");
        controller.loadGame("saveCollectedInvincibility");

        // Take invincibility potion
        DungeonResponse takeInvincibility = controller.tick("invincibility_potion6", Direction.NONE);
        assertFalse(takeInvincibility.getInventory().stream().anyMatch(ir -> ir.getType().equals("invincibility_potion")));

        // Walk for 4 ticks
        IntStream.range(0, 2).forEach(i -> {
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.UP);
        });

        // Test saving and loading
        controller.saveGame("saveTickingInvincibility");
        controller.loadGame("saveTickingInvincibility");

        // Test that effect wears off
        IntStream.range(0, 5).forEach(i -> {
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.UP);
        });
    }

    /**
     * Test consuming invisibility potion
     */
    @Test
    public void testTickInvisibility() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "standard");

        // Collect invisibility potion
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        // Test saving and loading
        controller.saveGame("saveCollectedInvisibility");
        controller.loadGame("saveCollectedInvisibility");

        // Take invisibility potion
        DungeonResponse takeInvisibility = controller.tick("invisibility_potion7", Direction.NONE);
        assertFalse(takeInvisibility.getInventory().stream().anyMatch(ir -> ir.getType().equals("invisibility_potion")));

        // Walk for 5 ticks
        IntStream.range(0, 5).forEach(i -> controller.tick(null, Direction.DOWN));

        // Test saving and loading
        controller.saveGame("saveTickingInvisibility");
        controller.loadGame("saveTickingInvisibility");

        // Test that effect wears off
        IntStream.range(0, 5).forEach(i -> {
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.UP);
        });
    }
}
