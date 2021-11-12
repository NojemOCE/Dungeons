package dungeonmania.logic;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dungeonmania.staticEntity.StaticEntity;

public class LogicLeaf extends LogicComponent {
    private boolean isActivated;

    public LogicLeaf() {
        this.isActivated = false;
    }

    public void activate() {
        this.isActivated = true;
        super.notifyObservers();
    }

    public void deactivate() {
        this.isActivated = false;
        super.notifyObservers();
    }

    @Override
    public boolean isActivated() {
        return isActivated;
    }

    @Override
    public JSONObject saveGameJson() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
