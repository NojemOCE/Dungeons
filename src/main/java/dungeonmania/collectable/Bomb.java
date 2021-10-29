package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;

public class Bomb extends CollectableEntity implements Consumable {


    public Bomb(int x, int y, String itemId) {
        super(x, y, itemId, "bomb");
    }

    public void consume() {
        decreaseDurability();
        drop();
    }

}
