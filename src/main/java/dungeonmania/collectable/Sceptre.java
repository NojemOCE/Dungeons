package dungeonmania.collectable;

import dungeonmania.MindControlled;

import java.util.HashMap;
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

}
