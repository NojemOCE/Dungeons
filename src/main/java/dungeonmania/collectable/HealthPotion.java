package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class HealthPotion extends CollectableEntity implements Consumable {
    
    private final double HEAL_EFFECT = 10;

    public HealthPotion(int x, int y, String itemId,Inventory inventory) {
        super(x, y, itemId, "health_potion", inventory);
    }

    public double heal() {
<<<<<<< HEAD
        consume();
=======
        getInventory().removeItem(getId());
>>>>>>> master
        return HEAL_EFFECT;
    }
    public void consume() {};
    

    public void consume() {
        getInventory().removeItem(getItemId());
    }

}
