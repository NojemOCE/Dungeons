package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity {
    
    private String itemId;
    private String type = "health_potion";
    private Inventory inventory;
    private final double HEAL_EFFECT = 10;

    public HealthPotion(Position position, String itemId, Inventory inventory) {
        super(position);
        this.itemId = itemId;
        this.inventory = inventory;
    }

    public double heal() {
        inventory.removeItem(itemId);
        return HEAL_EFFECT;
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
