package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {
    private boolean collected;
    private Position position;
    private String itemId;
    private String type;
    private Inventory inventory;



    public CollectableEntity(Position position, String id, String type, Inventory inventory) {
        super(position, id, type);
        this.collected = false;
        this.itemId = id;
        this.type = type;
        this.inventory = inventory;
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

    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {return collected;}
    
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
