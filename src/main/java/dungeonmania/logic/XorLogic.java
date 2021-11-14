package dungeonmania.logic;

import org.json.JSONObject;

public class XorLogic extends LogicComponent{

    /**
     * Construct for xor logic
     */
    public XorLogic(){}

    /**
     * For "xor", the entity will be activated if there is
     * 1 and only 1 adjacent activated switch
     * @return true if the component is activated, else false
     */
    public boolean isActivated() {
        int numActivated = 0;
        for (boolean activated : observing.values()) {
            if (activated) {
                numActivated++;
            }
        }

        // must be only 1
        return (numActivated == 1);
    }
    
    public String logicString() {
        return "xor";
    }
}
