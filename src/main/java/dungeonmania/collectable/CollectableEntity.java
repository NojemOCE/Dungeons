package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity {
    private boolean collected;
    private Position position;
    private String itemId;
    private String type;
    private Inventory inventory;

    public CollectableEntity() {
        this.position = null;
        this.collected = false;
    }

    public CollectableEntity(Position position, String itemId, String type) {
        this.position = position;
        this.collected = false;
        this.itemId = itemId;
        this.type = type;
        this.inventory = null;
    }

    public String getItemId() {
        return itemId;
    }

    public String getType() {
        return type;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void collect(Inventory inventory) {
        this.inventory = inventory;
        this.collected = true;
    }

    public boolean isCollected() {return collected;}

    public Position getPosition(){
        return position;
    }

    public void tick() {}

    public void updatePosition(Position position) {
        this.position = position;
    }

    public void drop() {
        this.collected = false;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(getItemId(), getType(), getPosition(), !isCollected());
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(getItemId(), getType());
    }
}
