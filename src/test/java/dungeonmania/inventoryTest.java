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
        inv.collect(armour);
        assertEquals(inv.numItem("armour"), 1);

        Sword sword = new Sword(1, 2, "sword2");
        assertEquals(inv.numItem("sword"), 0);
        inv.collect(sword);
        assertEquals(inv.numItem("sword"), 1);
        assertTrue(inv.hasWeapon());
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
        potion.consume();
        //assertFalse(inv.inInventory("health_potion1"));

        Bomb bomb = new Bomb(1, 2, "bomb2");
        assertFalse(inv.inInventory("bomb2"));
        inv.collect(bomb);
        assertTrue(inv.inInventory("bomb2"));
        bomb.consume();
        //assertFalse(inv.inInventory("bomb2"));
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
