package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvisibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;

    public InvisibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invisibility_potion");
    }

    public void applyPassive(Player player) {
        player.notifyPassive(getType());
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
