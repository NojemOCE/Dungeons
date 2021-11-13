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

    public Sceptre(int x, int y, String itemId) {
        super(x, y, itemId, "sceptre");
        controlled = new HashMap<>();
    }

    public Sceptre(String itemId, int cooldown, int durability) {
        this(0, 0, itemId, durability);
        this.cooldown = cooldown;
    }

    public Sceptre(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

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
    }

    /**
     * Overloaded method used when loading from a file
     * @param m given MercenaryComponent (Mercenary/Assassin)
     * @param duration remaining ticks from saved file
     */
    public void useMindControl(MercenaryComponent m, int duration) {
        controlled.put(m, duration);
        m.setAlly(true);
    }

    /**
     * Removes the MercenaryComponent from observer map when the effect duration is finished
     */
    public void notifyMindControlled() {
        for (MercenaryComponent m : controlled.keySet()) {
            if (controlled.get(m) == 0) {
                m.setAlly(false);
                controlled.remove(m);
            }
        }
    }

    public int getDuration(MercenaryComponent m) {
        return controlled.get(m);
    }

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
