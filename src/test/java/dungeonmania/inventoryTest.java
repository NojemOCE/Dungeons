package dungeonmania;

import dungeonmania.collectable.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inventoryTest {

    /**
     * When collecting an item, an item should appear in the inventory
     */
    @Test
    public void testCollectItem() {

        Inventory inv = new Inventory();

        Armour armour = new Armour(1, 1, "armour1");
        assertEquals(inv.numItem("armour"), 0);
        assertFalse(inv.inInventory("armour1"));
        inv.collect(armour);
        assertEquals(inv.numItem("armour"), 1);
        assertTrue(inv.inInventory("armour1"));

        Arrows arrows = new Arrows(1, 1, "arrows2");
        assertEquals(inv.numItem("arrow"), 0);
        assertFalse(inv.inInventory("arrows2"));
        inv.collect(arrows);
        assertEquals(inv.numItem("arrow"), 1);
        assertTrue(inv.inInventory("arrows2"));

        Bomb bomb = new Bomb(1, 1, "bomb3");
        assertEquals(inv.numItem("bomb"), 0);
        assertFalse(inv.inInventory("bomb3"));
        inv.collect(bomb);
        assertEquals(inv.numItem("bomb"), 1);
        assertTrue(inv.inInventory("bomb3"));

        HealthPotion hpot = new HealthPotion(1, 1, "health_potion4");
        assertEquals(inv.numItem("health_potion"), 0);
        assertFalse(inv.inInventory("health_potion4"));
        inv.collect(hpot);
        assertEquals(inv.numItem("health_potion"), 1);
        assertTrue(inv.inInventory("health_potion4"));

        InvincibilityPotion invinc = new InvincibilityPotion(1, 1, "invincibility_potion5", true);
        assertEquals(inv.numItem("invincibility_potion"), 0);
        assertFalse(inv.inInventory("invincibility_potion5"));
        inv.collect(invinc);
        assertEquals(inv.numItem("invincibility_potion"), 1);
        assertTrue(inv.inInventory("invincibility_potion5"));

        InvisibilityPotion invis = new InvisibilityPotion(1, 1, "invisibility_potion6");
        assertEquals(inv.numItem("invisibility_potion"), 0);
        assertFalse(inv.inInventory("invisibility_potion6"));
        inv.collect(invis);
        assertEquals(inv.numItem("invisibility_potion"), 1);
        assertTrue(inv.inInventory("invisibility_potion6"));

        Key key = new Key(1, 1, "key7", 0);
        assertEquals(inv.numItem("key"), 0);
        assertFalse(inv.inInventory("key7"));
        inv.collect(key);
        assertEquals(inv.numItem("key"), 1);
        assertTrue(inv.inInventory("key7"));
        assertNotNull(inv.keyInInventory(0));
        assertNull(inv.keyInInventory(1));

        OneRing ring = new OneRing(1, 1, "onering8");
        assertEquals(inv.numItem("one_ring"), 0);
        assertFalse(inv.inInventory("onering8"));
        inv.collect(ring);
        assertEquals(inv.numItem("one_ring"), 1);
        assertTrue(inv.inInventory("onering8"));

        Treasure treasure = new Treasure(1, 1, "treasure9");
        assertEquals(inv.numItem("treasure"), 0);
        assertFalse(inv.inInventory("treasure9"));
        inv.collect(treasure);
        assertEquals(inv.numItem("treasure"), 1);
        assertTrue(inv.inInventory("treasure9"));

        Wood wood = new Wood(1, 1, "wood10");
        assertEquals(inv.numItem("wood"), 0);
        assertFalse(inv.inInventory("wood10"));
        inv.collect(wood);
        assertEquals(inv.numItem("wood"), 1);
        assertTrue(inv.inInventory("wood10"));

        Anduril anduril = new Anduril(1, 1, "anduril11");
        assertEquals(inv.numItem("anduril"), 0);
        assertFalse(inv.inInventory("anduril11"));
        inv.collect(anduril);
        assertEquals(inv.numItem("anduril"), 1);
        assertTrue(inv.inInventory("anduril11"));

        SunStone sunstone = new SunStone(1, 1, "sunstone12");
        assertEquals(inv.numItem("sun_stone"), 0);
        assertFalse(inv.inInventory("sun_stone12"));
        inv.collect(sunstone);
        assertEquals(inv.numItem("sun_stone"), 1);
        assertTrue(inv.inInventory("sunstone12"));
    }

    /**
     * When consuming a consumable, it should no longer appear in the inventory
     */
    @Test
    public void testUseItem() {

        Inventory inv = new Inventory();

        HealthPotion potion = new HealthPotion(1, 1, "health_potion1");

        assertFalse(inv.inInventory("health_potion1"));
        inv.collect(potion);
        assertTrue(inv.inInventory("health_potion1"));
        inv.use(potion.getId());
        assertFalse(inv.inInventory("health_potion1"));

        Bomb bomb = new Bomb(1, 2, "bomb2");
        assertFalse(inv.inInventory("bomb2"));
        inv.collect(bomb);
        assertTrue(inv.inInventory("bomb2"));
        inv.use(bomb.getId());
        assertFalse(inv.inInventory("bomb2"));
    }

    /**
     * When consuming a sunstone for opening doors and bribing, the sunstone should remain in the inventory
     */
    @Test
    public void testUseSunStone() {

        Inventory inv = new Inventory();

        SunStone sunstone = new SunStone(1, 1, "sunstone1");

        assertFalse(inv.inInventory("sunstone1"));
        inv.collect(sunstone);
        assertTrue(inv.inInventory("sunstone1"));
        inv.use(sunstone.getId());
        assertTrue(inv.inInventory("sunstone1"));
    }

    /**
     * When crafting a bow, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */
    @Test
    public void testCraftBow() {

        Inventory inv = new Inventory();
        assert inv.numItem("wood") == 0;
        assert inv.numItem("arrow") == 0;

        Wood wood = new Wood(1, 2, "wood1");
        inv.collect(wood);
        assert inv.numItem("wood") == 1;

        Arrows arrow2 = new Arrows(1, 2, "arrow2");
        inv.collect(arrow2);
        assert inv.numItem("arrow") == 1;

        Arrows arrow3 = new Arrows(1, 3, "arrow3");
        inv.collect(arrow3);
        assert inv.numItem("arrow") == 2;

        Arrows arrow4 = new Arrows(1, 4, "arrow4");
        inv.collect(arrow4);
        assert inv.numItem("arrow") == 3;

        assert inv.getBuildable().contains("bow");
        inv.craft("bow", "5");

        assert inv.numItem("wood") == 0;
        assert inv.numItem("arrow") == 0;
        assert inv.numItem("bow") == 1;
    }

    /**
     * When crafting a shield, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */
    @Test
    public void testCraftShield() {

        Inventory inv = new Inventory();
        assert inv.numItem("wood") == 0;
        assert inv.numItem("treasure") == 0;

        Wood wood1 = new Wood(1, 2, "wood1");
        inv.collect(wood1);
        assert inv.numItem("wood") == 1;

        Wood wood2 = new Wood(1, 3, "wood2");
        inv.collect(wood2);
        assert inv.numItem("wood") == 2;

        Treasure treasure = new Treasure(1, 3, "treasure3");
        inv.collect(treasure);
        assert inv.numItem("treasure") == 1;

        assert inv.getBuildable().contains("shield");
        inv.craft("shield", "4");

        assert inv.numItem("wood") == 0;
        assert inv.numItem("treasure") == 0;
        assert inv.numItem("shield") == 1;
    }


    /**
     * When crafting a midnight armour, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */
    @Test
    public void testCraftMidnightArmour() {

        Inventory inv = new Inventory();
        assert inv.numItem("sun_stone") == 0;
        assert inv.numItem("armour") == 0;

        SunStone sunstone = new SunStone(1, 1, "sunstone1");
        inv.collect(sunstone);
        assert inv.numItem("sun_stone") == 1;

        Armour armour = new Armour(1, 1, "armour2");
        inv.collect(armour);
        assert inv.numItem("armour") == 1;

        assert inv.getBuildable().contains("midnight_armour");
        inv.craft("midnight_armour", "3");

        assert inv.numItem("sun_stone") == 0;
        assert inv.numItem("armour") == 0;
        assert inv.numItem("midnight_armour") == 1;
    }
    /**
     * Inventory doesn't allow the collecting of a key when one already exists within the inventory
     */
    @Test
    public void testMultipleKey() {
        Inventory inv = new Inventory();

        Key key1 = new Key(1, 1, "key1", 1);
        assertEquals(inv.numItem("key"), 0);
        assertFalse(inv.inInventory("key1"));
        inv.collect(key1);
        assertEquals(inv.numItem("key"), 1);
        assertTrue(inv.inInventory("key1"));

        Key key2 = new Key(1, 1, "key2", 1);
        assertFalse(inv.inInventory("key2"));
        inv.collect(key2);
        assertEquals(inv.numItem("key"), 1);
        assertFalse(inv.inInventory("key2"));

    }

    /**
     * When getCollectableItems is called, a map containing all collectable items within the inventory 
     * is returned 
     */
    @Test
    public void testGetCollectable() {
        Inventory inv = new Inventory();

        Map<String, CollectableEntity> expected = new HashMap<>();
        assertEquals(expected, inv.getCollectableItems());

        Key key = new Key(1, 1, "key1", 1);
        assertEquals(inv.numItem("key"), 0);
        assertFalse(inv.inInventory("key1"));
        inv.collect(key);
        assertEquals(inv.numItem("key"), 1);
        assertTrue(inv.inInventory("key1"));

        expected.put("key1", key);
        assertEquals(expected, inv.getCollectableItems());
    }

    /**
     * When crafting a shield, it should appear in the inventory
     * The items used to craft it should not appear in the inventory
     * Using wood and treasure to craft the shield in this test
     */
    @Test
    public void testCraftShieldWithTreasure() {

        Inventory inv = new Inventory();
        assert inv.numItem("wood") == 0;
        assert inv.numItem("key") == 0;

        Wood wood1 = new Wood(1, 2, "wood1");
        inv.collect(wood1);
        assert inv.numItem("wood") == 1;

        Wood wood2 = new Wood(1, 3, "wood2");
        inv.collect(wood2);
        assert inv.numItem("wood") == 2;

        Key key = new Key(1, 3, "key3", 1);
        inv.collect(key);
        assert inv.numItem("key") == 1;

        assert inv.getBuildable().contains("shield");
        inv.craft("shield", "4");

        assert inv.numItem("wood") == 0;
        assert inv.numItem("key") == 0;
        assert inv.numItem("shield") == 1;

    }

    /**
     * Removing a list of items within the inventory
     */
    @Test
    public void testRemoveMultipleItems() {
        Inventory inv = new Inventory();
        assert inv.numItem("wood") == 0;
        assert inv.numItem("key") == 0;

        Wood wood1 = new Wood(1, 2, "wood1");
        inv.collect(wood1);
        assert inv.numItem("wood") == 1;

        Wood wood2 = new Wood(1, 3, "wood2");
        inv.collect(wood2);
        assert inv.numItem("wood") == 2;

        Key key = new Key(1, 3, "key3", 1);
        inv.collect(key);
        assert inv.numItem("key") == 1;

        List<String> removeItems = new ArrayList<>();
        removeItems.add("wood1");
        removeItems.add("wood2");
        removeItems.add("key3");
        inv.removeItem(removeItems);

        assert inv.numItem("wood") == 0;
        assert inv.numItem("key") == 0;
    }

    /**
     * hasItem returns false when the provided item type doesn't exist within the inventory
     */
    @Test
    public void testNotHasItem() {
        Inventory inv = new Inventory();
        assertFalse(inv.hasItem("wood"));
    }

    /**
     * Check if getType provides the type of the item that corresponds with the provided item id
     */
    @Test
    public void testIsUsable() {
        Inventory inv = new Inventory();

        Wood wood1 = new Wood(1, 2, "wood1");
        inv.collect(wood1);
        assert inv.numItem("wood") == 1;
        assertEquals("wood", inv.getType(wood1.getId()));

        assertNull(inv.getType("wood2"));
    }

    /**
     * Check if an invalidActionException is thrown when the provided item id 
     * isn't present within the inventory
     */
    @Test
    public void testInvalidActionInventorytick() {
        Inventory inv = new Inventory();
        assertThrows(InvalidActionException.class, () -> inv.tick("wood1"));
    }

    /**
     * Check if an IllegalArgumentException is thrown when the provided item id 
     * doesn't correspond to a usable item
     */
    @Test
    public void testIllegalArgumentInventorytick() {
        Inventory inv = new Inventory();
        inv.collect(new Wood(1, 2, "wood1"));
        assertThrows(IllegalArgumentException.class, () -> inv.tick("wood1"));
    }

    /**
     * Checks if a valid usable item is consumed and removed from the inventory 
     * when tick is called with its corresponding item id
     */
    @Test
    public void testUsabletick() {
        Inventory inv = new Inventory();
        HealthPotion HPPot = new HealthPotion(1, 2, "health_potion1");
        inv.collect(HPPot);
        assertEquals(HPPot, inv.tick("health_potion1"));
        assert inv.numItem("health_potion") == 0;

        InvincibilityPotion InvincPot = new InvincibilityPotion(1, 2, "invincibility_potion2", false);
        inv.collect(InvincPot);
        assertEquals(InvincPot, inv.tick("invincibility_potion2"));
        assert inv.numItem("invincibility_potion") == 0;

        InvisibilityPotion InvisPot = new InvisibilityPotion(1, 2, "invisibility_potion3");
        inv.collect(InvisPot);
        assertEquals(InvisPot, inv.tick("invisibility_potion3"));
        assert inv.numItem("invisibility_potion") == 0;
    }

    /**
     * When crafting a midnight armour, it should appear in the inventory when there are no zombies within the world
     */
    @Test
    public void testCraftMidnightArmourWithoutZombie() {

        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("midnightArmourTest", "standard");

        // check that the midnight armour can't be crafted without the right materials
        assertThrows(InvalidActionException.class, () -> controller.build("midnight_armour"));

        // collecting the armour and sun stone used to craft the midnight armour
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        // crafting the midnight armour
        assertDoesNotThrow(() -> controller.build("midnight_armour"));
        DungeonResponse d = controller.tick(null, Direction.RIGHT);

        boolean MidnightArmourExists = false; 
        
        // checking if the midnight armour appears within the inventory
        for (ItemResponse item : d.getInventory()) {
            if (item.getType().equals("midnight_armour")) {
                MidnightArmourExists = true;
            }
        }

        assertTrue(MidnightArmourExists);
    }

    /**
     * When crafting a midnight armour, it should throw a InvalidActionException when there is a zombie
     * within the world
     */
    @Test
    public void testCraftMidnightArmourWithZombie() {

        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("midnightArmourTest", "standard");

        // check that the midnight armour can't be crafted without the right materials
        assertThrows(InvalidActionException.class, () -> controller.build("midnight_armour"));

        // collecting the armour and sun stone used to craft the midnight armour
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        // tick 20 times to spawn a zombie
        for (int i = 0; i <= 20; i++) {
            controller.tick(null, Direction.RIGHT);
        }

        // crafting the midnight armour when theres a zombie in the world
        assertThrows(InvalidActionException.class, () -> controller.build("midnight_armour"));
        DungeonResponse d = controller.tick(null, Direction.RIGHT);

        boolean MidnightArmourExists = false; 
        
        // checking if the midnight armour doesn't appear within the inventory
        for (ItemResponse item : d.getInventory()) {
            if (item.getType().equals("midnight_armour")) {
                MidnightArmourExists = true;
            }
        }

        assertFalse(MidnightArmourExists);
    }

}
