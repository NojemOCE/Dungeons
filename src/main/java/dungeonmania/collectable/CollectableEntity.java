package dungeonmania.collectable;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {
    private boolean collected;
    //private Position position;


    public CollectableEntity(Position position, String id, String type) {
        super(position, id, type);
        this.collected = false;
    }

    public void collect() {
        this.collected = true;
    };
    public boolean isCollected() {return collected;}
    
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), false);
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(getId(), getType());
    }
}
