package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;

import org.json.JSONObject;

import dungeonmania.Consumable;
import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity implements Consumable {
    private boolean collected;
    private String type;
    private Inventory inventory;
    private int durability = 1;

    public CollectableEntity(int x, int y, String id, String type) {
        super(new Position(x, y, 1), id, type);
        this.collected = false;
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

    public void consume() {
        this.durability--;
    }
    
    public void tick() {}

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int decreaseDurability() {
        return this.durability;
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

    @Override
    public JSONObject saveGameJson() {
        JSONObject saveObj = new JSONObject();
        saveObj.put("x", getPosition().getX());
        saveObj.put("y", getPosition().getY());
        saveObj.put("id", getId());
        saveObj.put("type", getType());
        saveObj.put("durability", durability);

        return saveObj;
    }
}
