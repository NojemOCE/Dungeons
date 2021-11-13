package dungeonmania.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class LogicComponent {
    protected Map<LogicComponent, Boolean> observing = new HashMap<>();
    protected List<LogicComponent> observers = new ArrayList<LogicComponent>();

    public abstract boolean isActivated();

    public void reset() {
        observing = new HashMap<>();
    }

    /**
     * Add observer to this logic component
     * @param lc logic component to add
     */
    public void addObserver(LogicComponent lc) {
        // make sure it is new
        if (observers.contains(lc)) {
            return;
        }
        observers.add(lc);
    }

    /**
     * Remove observer from this logic component
     * @param lc logic component to remove
     */
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

        if (status == null) {
            observing.remove(lc);
        } else {
            observing.put(lc, status);
            // if (observers.contains(lc)) {

            // }
        }

        // if state has changed
        if (!(isActivated() == initialState)) {
            notifyObservers(isActivated());
        }
    }

    /**
     * Notify observers of a change in status
     * @param status true for activated, false if not, and null if destroyed
     */
    public void notifyObservers(Boolean status) {
        for (LogicComponent lc : observers) {
            lc.update(this, status);
        }
    }

    /**
     * Get the string of the logic component
     * @return logic, e.g. "and, "or, ...
     */
    public abstract String logicString();
}
