package dungeonmania.collectable;
import org.json.JSONObject;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

import dungeonmania.Passive;
import dungeonmania.movingEntity.Player;

public class InvincibilityPotion extends CollectableEntity implements Passive {

    private final int DURATION = 3;
    private int duration = DURATION;
    private final double PLAYER_ATTACK = 3;
    private final double INVINCIBILITY_ATTACK = 999;

    public InvincibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invincibility_potion");
    }

    public void applyPassive(Player player) {
        if (this.duration == 0) {
            player.notifyPassive("N/A");
            player.setAttackDamage(PLAYER_ATTACK);
        } else {
            player.notifyPassive(getType());
            player.setAttackDamage(INVINCIBILITY_ATTACK);
        }
    }

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
        // TODO Auto-generated method stub
        return duration;
    }

}
