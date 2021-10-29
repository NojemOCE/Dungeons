package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Key extends CollectableEntity implements Consumable {
    private String keyColour;

    public Key(int x, int y, String itemId, Inventory inventory, String keyColour) {
        super(x, y, itemId, "key", inventory);
        this.keyColour = keyColour;
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

    public void craft() {
        getInventory().removeItem(getId());
    }

    /**
     * A key can only be picked up if there isn't already one in the inventory
     */
    @Override
    public void collect() {
        if (getInventory().numItem("key") == 0) {
            super.collect();
        }
    }

    public String getKeyColour() {
        return keyColour;
    }

}
