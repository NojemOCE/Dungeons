package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity implements Consumable {
    
    private final double HEAL_EFFECT = 10;

    public HealthPotion(Position position, String itemId) {
        super(position, itemId, "health_potion");
    }

    public double heal() {
        consume();
        return HEAL_EFFECT;
    }

    public void consume() {
        getInventory().removeItem(getItemId());
    }

}
