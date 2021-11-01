package dungeonmania;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryControllerTesting {

    /**
     * When collecting an item, an item should appear in the inventory
     */
    @Test
    public void testCollectItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "Standard");
        DungeonResponse CurrDungResp = controller.tick(null, Direction.DOWN);

        boolean contains = false;
        for (ItemResponse ir : CurrDungResp.getInventory()) {
            if (ir.getType().equals("health_potion")) {
                contains = true;
                break;
            }
        }
        assertTrue(contains);

    }

    /**
     * When consuming a consumable, it should no longer appear in the inventory
     */
    @Test
    public void testConsumeItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("collectable-world", "Standard");
        
        controller.tick(null, Direction.DOWN);
        DungeonResponse CurrDungResp = controller.tick("health_potion", null);
        
        boolean contains = false;
        for (ItemResponse ir : CurrDungResp.getInventory()) {
            if (ir.getType().equals("health_potion")) {
                contains = true;
                break;
            }
        }
        assertFalse(contains);

    }

    /**
     * When crafting a bow, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */

    /**
     * When crafting a shield, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */

    /**
     * Ensure that Buildable list updates correctly
     * as items are collected
     */

}
