package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {
    private Door door;
    private String keyColour;

    public Key(Position position, String itemId, Inventory inventory, String keyColour) {
        super(position, itemId, "key", inventory);
        this.keyColour = keyColour;
    }

    public void consume() {
        getInventory().removeItem(getItemId());
    }

    public void craft() {
        getInventory().removeItem(getItemId());
    }

    public void unlock() {
        getInventory().removeItem(getItemId());
        door.open();
    }

    public String getKeyColour() {
        return keyColour;
    }

}
