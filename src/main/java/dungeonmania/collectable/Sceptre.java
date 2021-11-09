package dungeonmania.collectable;

import dungeonmania.MindControlled;
import dungeonmania.movingEntity.MovingEntity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class Sceptre extends CollectableEntity {
    final int DURATION = 10;
    private Map<MindControlled, Integer> controlled;

    public Sceptre(int x, int y, String itemId) {
        super(x, y, itemId, "sceptre");
        controlled = new HashMap<>();
    }

    public void useMindControl(MindControlled m) {
        controlled.put(m, DURATION);
        m.update(this);
    }

    // For when loading, setting a duration
    public void useMindControl(MindControlled m, int duration) {
        controlled.put(m, duration);
        m.update(this);
    }

    public void updateMindControlled() {
        controlled.replaceAll((m, v) -> v - 1);
        notifyMindControlled();
    }

    public void notifyMindControlled() {
        for (MindControlled m : controlled.keySet()) {
            if (controlled.get(m) == 0) {
                m.update(this);
                controlled.remove(m);
            }
        }
    }

    public boolean isMindControlled(MindControlled m) {
        return !isNull(controlled.get(m));
    }

    // Loading game (mounting)
        // Adding it to a list with a duration

    @Override
    public JSONObject saveGameJson() {
        JSONObject sceptreJSON  = super.saveGameJson();
        List<String> controlledIds = new ArrayList<>();
        for (MindControlled m : controlled.keySet()) {
            controlledIds.add(((MovingEntity) m).getId());
        }
        sceptreJSON.put("controlled", controlledIds);
        return sceptreJSON;
    }

}
