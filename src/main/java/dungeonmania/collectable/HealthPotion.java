package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity {
    
    private final double HEAL_EFFECT = 10;

    public HealthPotion(Position position, String itemId,Inventory inventory) {
        super(position, itemId, "health_potion", inventory);
    }

    public double heal() {
        getInventory().removeItem(getId());
        return HEAL_EFFECT;
    }
    public void consume() {};
    

}
