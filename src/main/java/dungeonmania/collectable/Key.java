package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Key extends CollectableEntity {
    private String keyColour;

    public Key(int x, int y, String itemId, String keyColour) {
        super(x, y, itemId, "key");
        this.keyColour = keyColour;
    }

    public String getKeyColour() {
        return keyColour;
    }

}
