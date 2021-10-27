package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity implements Consumable {

    public Treasure(Position position, String itemId) {
        super(position, itemId, "treasure");
    }

    public void consume() {
        getInventory().removeItem(getItemId());
    }

}
