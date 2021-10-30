package dungeonmania.collectable;

public class HealthPotion extends CollectableEntity {
    
    private final double HEAL_EFFECT = 10;

    public HealthPotion(int x, int y, String itemId) {
        super(x, y, itemId, "health_potion");
    }

    public double getHeal() {
        return HEAL_EFFECT;
    }

    @Override
    public CollectableEntity consume() {
        decreaseDurability();
        return this;
    }
}
