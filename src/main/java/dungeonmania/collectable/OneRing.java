package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class OneRing extends CollectableEntity implements Consumable {

    public OneRing(Position position, String itemId, Inventory inventory) {
        super(position, itemId, "one_ring", inventory);
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

}
