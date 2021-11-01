package dungeonmania.collectable;

import org.json.JSONObject;

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
    
    public void applyPassive(Player player) {
        if (this.duration == 0) {
            player.notifyPassive("N/A");
        } else {
            player.notifyPassive(getType());
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

    @Override
    public JSONObject saveGameJson() {
        JSONObject saveObj = super.saveGameJson();

        saveObj.put("duration", duration);
        
        return saveObj;
    }

}
