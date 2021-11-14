package dungeonmania.logic;

import org.json.JSONObject;

public class NotLogic extends LogicComponent{

    /**
     * For "not", the entity will be activated if there are 
     * 0 adjacent activated switches. Bombs cannot be produced 
     * with this logic.
     * @return true if the component is activated, else false
     */
    public boolean isActivated() {
        for (boolean activated : observing.values()) {
            if (activated) {
                return false;
            }
        }
        return true;
    }
    
    public String logicString() {
        return "not";
    }
}
