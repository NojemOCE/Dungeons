package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Wood extends CollectableEntity implements Consumable {

    public Wood(Position position, String itemId, Inventory inventory) {
        super(position, itemId, "wood", inventory);
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

}
