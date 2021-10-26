package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity implements Consumable {

    private String itemId;
    private String type = "treasure";
    private Inventory inventory;

    public Treasure(Position position, String itemId, Inventory inventory) {
        super(position);
        this.itemId = itemId;
        this.inventory = inventory;
    }

    public void consume() {
        inventory.removeItem(itemId);
    };

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(itemId, type, getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
