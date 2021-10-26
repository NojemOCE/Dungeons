package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity {

    private String itemId;
    private String type = "armour";
    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(Position position, String itemId) {
        super(position);
        this.itemId = itemId;
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    };

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(itemId, type, getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
