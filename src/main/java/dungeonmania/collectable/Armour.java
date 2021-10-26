package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity {

    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(Position position, String itemId) {
        super(position, itemId, "armour");
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    };

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getItemId(), getType(), getPosition(), !isCollected());
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(this.getItemId(), this.getType());
    }
}
