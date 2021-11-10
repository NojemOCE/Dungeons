package dungeonmania.collectable;

import dungeonmania.movingEntity.MercenaryComponent;
import dungeonmania.movingEntity.MovingEntity;
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

    public boolean isMindControlled(MercenaryComponent m) {
        return !isNull(controlled.get(m));
    }

    /*
    @Override
    public JSONObject saveGameJson() {
        JSONObject sceptreJSON  = super.saveGameJson();
        List<String> controlledIds = new ArrayList<>();
        for (MercenaryComponent m : controlled.keySet()) {

        }
        sceptreJSON.put("controlled", controlledIds);
        return sceptreJSON;
    }
    */
}
