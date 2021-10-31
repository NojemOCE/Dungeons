package dungeonmania.collectable;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvincibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 10;
    private int duration = DURATION;
    private final int PLAYER_ATTACK = 3;
    private final int INVINCIBILITY_ATTACK = 999;

    public InvincibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invincibility_potion");
    }

    public InvincibilityPotion(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public InvincibilityPotion(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public void applyPassive(Player player) {
        player.notifyPassive(getType());
        if (this.duration == 0) {
            player.setAttackDamage(PLAYER_ATTACK);
        } else {
            player.setAttackDamage(INVINCIBILITY_ATTACK);
        }
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
