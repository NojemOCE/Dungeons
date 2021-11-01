package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvisibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;

    public InvisibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invisibility_potion");
    }

    public InvisibilityPotion(String itemId, int durability) {
        this(0, 0, itemId);
        setDurability(durability);
    }

    public InvisibilityPotion(int x, int y, String itemId, int durability, int duration) {
        this(x, y, itemId);
        setDurability(durability);
        this.duration = duration;
    }

    public InvisibilityPotion(int duration) {
        this(0,0, null);
        this.duration = duration;
    }
    
    /**
     * Notifies the player observer about whether the potion is active or not
     */
    public void applyPassive(Player player) {
        if (this.duration == 0) {
            player.notifyPassive("N/A");
        } else {
            player.notifyPassive(getType());
        }
    }

    /**
     * Decrements the duration of the potion by 1
     */
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
