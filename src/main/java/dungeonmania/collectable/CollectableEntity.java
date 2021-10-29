package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {
    private boolean collected;
    private String type;
    private int durability;



    public CollectableEntity(int x, int y, String id, String type) {
        super(new Position(x, y, 1), id, type);
        this.collected = false;
        this.type = type;
    }

    /**
     * Pick up this item
     */
    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }
    
    public void tick() {}

    public void drop() {
        this.collected = false;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void decreaseDurability() {
        this.durability--;
    }

    public int getDurability() {
        return this.durability;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), false);
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(getId(), getType());
    }
}
