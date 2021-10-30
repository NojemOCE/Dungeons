package dungeonmania;

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

        Armour armour = new Armour(1, 1, "armour1");
        assert inv.numItem("armour") == 0;
        inv.collect(armour);
        assert inv.numItem("armour") == 1;

        Sword sword = new Sword(1, 2, "sword2");
        assert inv.numItem("sword") == 0;
        inv.collect(sword);
        assert inv.numItem("sword") == 1;
        assertTrue(inv.hasWeapon());
    }

    /**
     * When consuming a consumable, it should no longer appear in the inventory
     */
    @Test
    public void testUseItem() {

        Inventory inv = new Inventory();

        Position p = new Position(1,1);
        HealthPotion potion = new HealthPotion(1, 1, "health_potion1");

        assert inv.numItem("health_potion1") == 0;
        inv.collect(potion);
        assert inv.numItem("health_potion1") == 1;
        potion.consume();
        assert inv.numItem("health_potion1") == 0;

        Position p2 = new Position(1,2);
        Bomb bomb = new Bomb(1, 2, "bomb2");
        assert inv.numItem("bomb") == 0;
        inv.collect(bomb);
        assert inv.numItem("bomb") == 1;
        bomb.consume();
        assert inv.numItem("bomb") == 0;
    }

    /**
     * When crafting items, the buildable should appear in the inventory
     * The items used to craft it should not appear in the inventory
     */
    @Test
    public void testCraftItem() {

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

        inv.craft("bow", "5");

        assert inv.numItem("wood") == 0;
        assert inv.numItem("arrow") == 0;
        assert inv.numItem("bow") == 1;
    }
}
