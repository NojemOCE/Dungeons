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

    public void applyPassive(Player player) {
        System.out.println("HEAL!");
        player.addHealth(HEAL_EFFECT);
    }

    public void decreaseDuration() {
        duration--;
    }

    public int getDuration() {
        return this.duration;
    }


    @Override
    public CollectableEntity consume() {
        decreaseDurability();
        return this;
    }
}
