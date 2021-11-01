package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class HealthPotion extends CollectableEntity implements Passive {
    
    private final double HEAL_EFFECT = 10;
    private final int DURATION = 1;
    private int duration = DURATION;

    public HealthPotion(int x, int y, String itemId) {
        super(x, y, itemId, "health_potion");
    }

    public HealthPotion(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public HealthPotion(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public HealthPotion(int duration) {
        this(0,0, null);
        this.duration = duration;
    }

    /**
     * Increases the player's health points by the HEAL_EFFECT of this potion
     */
    public void applyPassive(Player player) {
        player.addHealth(HEAL_EFFECT);
    }

    /**
     * Decrements the duration of the potion by 1
     */
    public void decreaseDuration() {
        duration--;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }


    @Override
    public CollectableEntity consume() {
        decreaseDurability();
        return this;
    }
}
