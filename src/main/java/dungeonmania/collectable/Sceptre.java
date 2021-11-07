package dungeonmania.collectable;

import dungeonmania.movingEntity.MovingEntity;

import java.util.HashMap;
import java.util.Map;

public class Sceptre extends CollectableEntity {
    final int DURATION = 10;
    private Map<MovingEntity, Integer> controlled;

    public Sceptre(int x, int y, String itemId) {
        super(x, y, itemId, "sceptre");
        controlled = new HashMap<>();
    }

    public void useMindControl(MovingEntity m) {
        controlled.put(m, DURATION);
        m.setAlly(false);
    }

    public void updateMindControlled() {
        controlled.replaceAll((m, v) -> v - 1);
    }

    public void notifyMindControlled() {
        for (MovingEntity m : controlled.keySet()) {
            if (controlled.get(m) == 0) {
                m.setAlly(false);
                controlled.remove(m);
            }
        }
    }

}
