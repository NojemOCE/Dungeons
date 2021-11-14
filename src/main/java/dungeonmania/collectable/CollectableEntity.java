package dungeonmania.collectable;

import org.json.JSONObject;

import dungeonmania.Consumable;
import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity implements Consumable {
    private boolean collected;
    private int durability = 1;

    /**
     * Constructor for a collectable entity taking an x coordinate, a y coordinate, unique id and string type
     * @param x x coordinate of entity
     * @param y y coordinate of entity
     * @param id unique id of entity
     * @param type string type of entity
     */
    public CollectableEntity(int x, int y, String id, String type) {
        super(new Position(x, y, Position.STATIC_LAYER), id, type);
        this.collected = false;
    }

    /**
     * Signals that the current item has been picked up by setting it's collected variable to true
     */
    public void collect() {
        this.collected = true;
    }

    /**
     * Returns whether the current item has been collected
     * @return true if the item has been collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Decrements the durability of the current item by 1
     * @return a CollectableEntity which is set as null for the default implementation
     */
    public CollectableEntity consume() {
        decreaseDurability();
        return null;
    }

    /**
     * Sets the durability of the collectable entity to a given value
     * @param durability durability to set to
     */
    public void setDurability(int durability) {
        this.durability = durability;
    }

    /**
     * Decrements the durabaility of the current item by 1
     */
    public void decreaseDurability() {
        this.durability--;
    }
    
    /**
     * Gets the current durability of the collectable entity
     * @return current durabbility
     */
    public int getDurability() {
        return this.durability;
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), false);
    }

    /**
     * Creates and returns an item response for the given collectable entity
     * @return item response for collectable entity
     */
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
