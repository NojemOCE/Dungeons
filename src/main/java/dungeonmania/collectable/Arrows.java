package dungeonmania.collectable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Arrows extends CollectableEntity {

    public Arrows(Position position, String itemId) {
        super(position, itemId, "arrow");
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(this.getItemId(), this.getType(), getPosition(), !isCollected());
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(this.getItemId(), this.getType());
    }
}
