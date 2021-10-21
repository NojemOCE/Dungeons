package dungeonmania;

import dungeonmania.buildable.*;
import dungeonmania.collectable.*;
import dungeonmania.inventory.Inventory;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class inventoryTest {

    /**
     * When collecting an item, an item should appear in the inventory
     */
    @Test
    public void testCollectItem() {

        Inventory inv = new Inventory();
        Position p = new Position(1,1);
        
        Armour armour = new Armour(p);
        inv.collect(armour);
        assertTrue(inv.isPresent(armour));

        Position p2 = new Position(1,2);
        Sword sword = new Sword(p2);
        
        inv.collect(sword);
        assertTrue(inv.isPresent(sword));
    }

    /**
     * When consuming a consumable, it should no longer appear in the inventory
     */
    @Test
    public void testUseItem() {

        Inventory inv = new Inventory();

        Position p = new Position(1,1);
        HealthPotion potion = new HealthPotion(p);

        inv.collect(potion);
        assertTrue(inv.isPresent(potion));
        potion.consume();
        assertFalse(inv.isPresent(potion));

        Position p2 = new Position(1,2);
        Bomb bomb = new Bomb(p2);
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

        Position p = new Position(1,1);
        Wood wood = new Wood(p);
        inv.collect(wood);
        assertTrue(inv.isPresent(wood));

        Position p2 = new Position(1,2);
        Arrows arrow1 = new Arrows(p2);
        inv.collect(arrow1);
        assertTrue(inv.isPresent(arrow1));

        Position p3 = new Position(1,3);
        Arrows arrow2 = new Arrows(p3);
        inv.collect(arrow2);
        assertTrue(inv.isPresent(arrow2));

        Position p4 = new Position(1,4);
        Arrows arrow3 = new Arrows(p4);
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
