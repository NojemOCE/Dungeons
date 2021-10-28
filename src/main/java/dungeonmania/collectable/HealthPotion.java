package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;

public class HealthPotion extends CollectableEntity {
    
    private final double HEAL_EFFECT = 10;

    public HealthPotion(int x, int y, String itemId,Inventory inventory) {
        super(x, y, itemId, "health_potion", inventory);
    }

    public double heal() {
        getInventory().removeItem(getId());
        return HEAL_EFFECT;
    }
    public void consume() {};
    

}
