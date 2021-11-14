package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvincibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;
    private final int PLAYER_ATTACK = 3;
    private final int INVINCIBILITY_ATTACK = 999;
    private boolean enabled;

    /**
     * Constructor for invincibility potion taking an x and a y coordinate and its unique itemID and boolean for is invincibility enabled
     * @param x x coordinate of invincibility potion
     * @param y y coordinate of invincibility potion
     * @param itemId unique item id of invincibility potion
     * @param isInvincibilityEnabled boolean for whether invincibility is enabled
     */
    public InvincibilityPotion(int x, int y, String itemId, boolean isInvincibilityEnabled) {
        super(x, y, itemId, "invincibility_potion");
        this.enabled = isInvincibilityEnabled;

    }

    /**
     * Constructor for invincibility potion taking its unique itemID and durability and boolean for is invincibility enabled
     * @param itemId unique item id of invincibility potion
     * @param durability integer value of durability
     * @param isInvincibilityEnabled boolean for whether invincibility is enabled
     */
    public InvincibilityPotion(String itemId, int durability, boolean isInvincibilityEnabled) {
        this(0, 0, itemId, isInvincibilityEnabled);
    }

    /**
     * Constructor for invincibility potion taking an x and a y coordinate and its unique itemID, durability and boolean for is invincibility enabled
     * @param x x coordinate of invincibility potion
     * @param y y coordinate of invincibility potion
     * @param itemId unique item id of invincibility potion
     * @param durability integer value of durability
     * @param isInvincibilityEnabled boolean for whether invincibility is enabled
     */
    public InvincibilityPotion(int x, int y, String itemId, int durability,int duration, boolean isInvincibilityEnabled) {
        this(x, y, itemId, isInvincibilityEnabled);
        setDurability(durability);
        this.duration = duration;
    }

    /**
     * Constructor for invincibility potion taking in a duration
     * @param duration duration of invincibility potion
     */
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

    @Override
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
