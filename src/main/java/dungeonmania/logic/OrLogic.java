package dungeonmania.logic;


public class OrLogic extends LogicComponent{

    /**
     * Construct for or logic
     */
    public OrLogic(){}

    /**
     * For "or", the entity will be activated if there 
     * are 1 or more adjacent activated switches
     * @return true if the component is activated, else false
     */
    public boolean isActivated() {
        for (boolean activated : observing.values()) {
            if (activated) {
                return true;
            }
        }
        return false;
    }
    
    public String logicString() {
        return "or";
    }

}
