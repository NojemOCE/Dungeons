package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class OneRing extends CollectableEntity implements Consumable {

    public OneRing(int x, int y, String itemId, Inventory inventory) {
        super(x, y, itemId, "one_ring", inventory);
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

}
