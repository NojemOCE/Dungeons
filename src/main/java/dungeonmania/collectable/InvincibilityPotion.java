package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvincibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;
    private final int PLAYER_ATTACK = 3;
    private final int INVINCIBILITY_ATTACK = 999;
    private boolean enabled;

    public InvincibilityPotion(int x, int y, String itemId, boolean isInvincibilityEnabled) {
        super(x, y, itemId, "invincibility_potion");
        this.enabled = isInvincibilityEnabled;

    }

    public InvincibilityPotion(String itemId, int durability, boolean isInvincibilityEnabled) {
        this(0, 0, itemId, isInvincibilityEnabled);
    }

    public InvincibilityPotion(int x, int y, String itemId, int durability,int duration, boolean isInvincibilityEnabled) {
        this(x, y, itemId, isInvincibilityEnabled);
        setDurability(durability);
        this.duration = duration;
    }

    public InvincibilityPotion(int duration) {
        super(0, 0, null, "invincibility_potion");
        this.duration = duration;
    }

    /**
     * Sets the attack damage of the player depending on whether the invincibility potion is active
     * and notifies the observer about whether the potion is active or not
     */
    public void applyPassive(Player player) {
        if (enabled) {
            if (this.duration == 0) {
                player.notifyPassive("N/A");
                player.setAttackDamage(PLAYER_ATTACK);
            } else {
                player.notifyPassive(getType());
                player.setAttackDamage(INVINCIBILITY_ATTACK);
            }
        }
    }

    /**
     * Decrements the duration of the potion by 1
     */
    public void decreaseDuration() {
        duration--;
    }

    @Override
    public CollectableEntity consume() {
        decreaseDurability();
        return this;
    }

    @Override
    public int getDuration() {
        return duration;
    }


}
