package dungeonmania.logic;

public class NoLogic extends LogicComponent{

    /**
     * Construct for no logic
     */
    public NoLogic(){}

    /**
     * A no logic component will never be activated
     * @return false
     */
    public boolean isActivated() {
        return false;
    }
    
    public String logicString() {
        return "no";
    }
}
