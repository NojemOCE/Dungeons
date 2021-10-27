package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {
    private Door door;

    public Key(Position position, String itemId) {
        super(position, itemId, "key");
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

}
