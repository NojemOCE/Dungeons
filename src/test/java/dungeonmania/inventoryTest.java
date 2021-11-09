package dungeonmania;

import dungeonmania.collectable.*;
import dungeonmania.inventory.Inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
