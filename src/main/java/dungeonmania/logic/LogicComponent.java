package dungeonmania.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.staticEntity.StaticEntity;


public abstract class LogicComponent {
    protected Map<LogicComponent, Boolean> observing;
    protected List<LogicComponent> observers = new ArrayList<LogicComponent>();

    public abstract boolean isActivated();
    public abstract JSONObject saveGameJson();

    public void addObserver(LogicComponent e) {
        observers.add(e);
    }

    public void removeObserver(LogicComponent e) {
        observers.remove(e);
    }

    /**
     * Update the activation status in our observing map
     * @param e
     * @param status
     */
    public void update(LogicComponent e, boolean status) {
        observing.put(e, status);
    }


    protected void notifyObservers() {
        for (LogicComponent lc : observers) {
            lc.update(this, this.isActivated());
        }
    }

    // protected Map<LogicComponent, Boolean> getObserving
}
