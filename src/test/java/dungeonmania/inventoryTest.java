package dungeonmania;

import dungeonmania.buildable.*;
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

        Armour armour = new Armour();
        inv.collect(armour);
        assertTrue(inv.isPresent(armour));

        Sword sword = new Sword();
        inv.collect(sword);
        assertTrue(inv.isPresent(sword));
    }

    /**
     * When consuming a consumable, it should no longer appear in the inventory
     */
    @Test
    public void testUseItem() {

        Inventory inv = new Inventory();

        HealthPotion potion = new HealthPotion();
        inv.collect(potion);
        assertTrue(inv.isPresent(potion));
        potion.consume();
        assertFalse(inv.isPresent(potion));

        Bomb bomb = new Bomb();
        inv.collect(bomb);
        assertTrue(inv.isPresent(bomb));
        bomb.consume();
        assertFalse(inv.isPresent(bomb));
    }

    /**
     * When crafting items, the buildable should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */
    @Test
    public void testCraftItem() {

        Inventory inv = new Inventory();

        Wood wood = new Wood();
        inv.collect(wood);
        assertTrue(inv.isPresent(wood));

        Arrows arrow1 = new Arrows();
        inv.collect(arrow1);
        assertTrue(inv.isPresent(arrow1));

        Arrows arrow2 = new Arrows();
        inv.collect(arrow2);
        assertTrue(inv.isPresent(arrow2));

        Arrows arrow3 = new Arrows();
        inv.collect(arrow3);
        assertTrue(inv.isPresent(arrow3));

        Bow bow = new Bow();
        inv.craft(bow);

        assertFalse(inv.isPresent(wood));
        assertFalse(inv.isPresent(arrow1));
        assertFalse(inv.isPresent(arrow2));
        assertFalse(inv.isPresent(arrow3));
        assertTrue(inv.isPresent(bow));

    }

}
