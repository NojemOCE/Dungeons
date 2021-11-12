package dungeonmania.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class LogicComponent {
    protected Map<LogicComponent, Boolean> observing = new HashMap<>();
    protected List<LogicComponent> observers = new ArrayList<LogicComponent>();

    public abstract boolean isActivated();

    public void addObserver(LogicComponent lc) {
        observers.add(lc);
    }

    public void removeObserver(LogicComponent lc) {
        observers.remove(lc);
    }

    /**
     * Update the activation status in our observing map.
     * If activation state changes, we notify observers
     * @param lc The logic component that has updated
     * @param status The current activation status, or null if entity has been destroyed
     */
    public void update(LogicComponent lc, Boolean status) {
        boolean initialState = isActivated();

        if (status.equals(null)) {
            observing.remove(lc);
        } else {
            observing.put(lc, status);
        }

        // if state has changed
        if (!(isActivated() == initialState)) {
            notifyObservers(isActivated());
        }
    }


    public void notifyObservers(boolean status) {
        for (LogicComponent lc : observers) {
            lc.update(this, status);
        }
    }

    public abstract String logicString();
}
