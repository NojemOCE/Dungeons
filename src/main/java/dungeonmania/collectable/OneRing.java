package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class OneRing extends CollectableEntity implements Consumable {

    private String itemId;
    private String type = "one_ring";
    private Inventory inventory;

    public OneRing(Position position, String itemId, Inventory inventory) {
        super(position);
        this.itemId = itemId;
        this.inventory = inventory;
    }

    public void consume() {
        inventory.removeItem(itemId);
    }
    
    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(itemId, type, getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
