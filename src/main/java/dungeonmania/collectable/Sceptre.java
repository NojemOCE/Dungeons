package dungeonmania.collectable;

import dungeonmania.movingEntity.MercenaryComponent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sceptre extends CollectableEntity {
    final int DURATION = 10;
    private Map<MercenaryComponent, Integer> controlled;
    private int cooldown = 0;

    /**
     * Constructor for sceptre taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sceptre
     * @param y y coordinate of sceptre
     * @param itemId unique item id of sceptre
     */
    public Sceptre(int x, int y, String itemId) {
        super(x, y, itemId, "sceptre");
        controlled = new HashMap<>();
    }

    /**
     * Constructor for sceptre taking its unique id, cooldown and durability
     * @param itemId unique item id of sceptre
     * @param cooldown cooldown of sceptre
     * @param durability durability of sceptre
     */
    public Sceptre(String itemId, int cooldown, int durability) {
        this(0, 0, itemId, durability);
        this.cooldown = cooldown;
    }

    /**
     * Constructor for sceptre taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sceptre
     * @param y y coordinate of sceptre
     * @param itemId unique item id of sceptre
     * @param durability integer value of durability
     */
    public Sceptre(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * Sets the cooldown period of the sceptre to a given value
     * @param cooldown int value of cooldown
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Provide the current cooldown of the sceptre
     * @return current cooldown of the sceptre
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Decrement the sceptre cooldown every tick
     */
    public void tickCooldown() {
        if (getCooldown() > 0) setCooldown(getCooldown() - 1);
    }

    /**
     * Mind control a particular MercenaryComponent (Mercenary/Assassin)
     * Sets the sceptre cooldown to 15 ticks
     * Sets the effect to 10 ticks
     * @param m given MercenaryComponent (Mercenary/Assassin)
     */
    public void useMindControl(MercenaryComponent m) {
        setCooldown(15);
        useMindControl(m, DURATION);
        notifyMindControlled();
    }

    /**
     * Overloaded method used when loading from a file
     * @param m given MercenaryComponent (Mercenary/Assassin)
     * @param duration remaining ticks from saved file
     */
    public void useMindControl(MercenaryComponent m, int duration) {
        controlled.put(m, duration);
    }

    /**
     * Removes the MercenaryComponent from observer map when the effect duration is finished
     */
    public void notifyMindControlled() {
        for (MercenaryComponent m : controlled.keySet()) {
            m.updateDuration(controlled.get(m));
            if (controlled.get(m) == 0) controlled.remove(m);
        }
    }

    /**
     * Get remaining duration of mind control effect
     * @param m given MercenaryComponent (Mercenary/Assassin)
     * @return remaining duration of effect
     */
    public int getDuration(MercenaryComponent m) {
        return controlled.get(m);
    }

    /**
     * Get JSONArray of mind controlled entities
     * @return JSONArray of mind controlled entities
     */
    public JSONArray getMindControlled() {

        JSONArray controlledJSON = new JSONArray();
        for (MercenaryComponent m : controlled.keySet()) {
            JSONObject entity = new JSONObject();
            entity.put("id", m.getId());
            entity.put("duration", getDuration(m));
            controlledJSON.put(entity);
        }
        return controlledJSON;
    }

    /**
     * Determine if the sceptre is ready
     * @return true if sceptre is ready
     */
    public boolean ready() {
        return cooldown == 0;
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject sceptreJSON  = super.saveGameJson();
        sceptreJSON.put("cooldown", "0");
        return sceptreJSON;
    }

    @Override
    public CollectableEntity consume() {
        tickCooldown();
        controlled.replaceAll((m, v) -> v - 1);
        notifyMindControlled();
        return null;
    }
}
