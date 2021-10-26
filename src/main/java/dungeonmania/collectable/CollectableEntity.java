package dungeonmania.collectable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public abstract class CollectableEntity {
    private boolean collected;
    private Position position;

    public CollectableEntity() {
        this.position = null;
        this.collected = false;
    }
    
    public CollectableEntity(Position position) {
        this.position = position;
        this.collected = false;
    }
    public void collect() {
        this.collected = true;
    };
    public boolean isCollected() {return collected;}

    public Position getPosition(){
        return position;
    }

    
    abstract public EntityResponse getEntityResponse();
    abstract public ItemResponse getItemResponse();
}