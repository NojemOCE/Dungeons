package dungeonmania.collectable;

import dungeonmania.movingEntity.MercenaryComponent;
import dungeonmania.movingEntity.MovingEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class Sceptre extends CollectableEntity {
    final int DURATION = 10;
    private Map<MercenaryComponent, Integer> controlled;

    public Sceptre(int x, int y, String itemId) {
        super(x, y, itemId, "sceptre");
        controlled = new HashMap<>();
    }

    public Sceptre(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Sceptre(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    public void useMindControl(MercenaryComponent m) {
        useMindControl(m, DURATION);
    }

    // For when loading, setting a duration
    public void useMindControl(MercenaryComponent m, int duration) {
        controlled.put(m, duration);
        m.setAlly(true);
    }

    public void tickMindControlled() {
        controlled.replaceAll((m, v) -> v - 1);
        notifyMindControlled();
    }

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

    public boolean isMindControlled(MercenaryComponent m) {
        return !isNull(controlled.get(m));
    }

    public JSONArray getMindControlled() {

        JSONArray controlledJSON = new JSONArray();
        for (MercenaryComponent m : controlled.keySet()) {
            JSONObject entity = new JSONObject();
            entity.put("id", m.getId());
            entity.put("duration", getDuration(m));
        }

        return controlledJSON;
    }

}
