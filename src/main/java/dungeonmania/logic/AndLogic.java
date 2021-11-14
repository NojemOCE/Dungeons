package dungeonmania.logic;

public class AndLogic extends LogicComponent {

    /**
     * Construct for and logic
     */
    public AndLogic(){}

    /**
     * For "and", the entity will be only activated if there are 2 or 
     * more adjacent activated switches (switches with boulders on them). 
     * If there are more than two switches adjacent, all must be activated.
     * @return true if the component is activated, else false
     */
    public boolean isActivated() {
        int numActivated = 0;
        for (boolean activated : observing.values()) {
            if (activated) {
                numActivated++;
            }
        }

        // must have at least 2 activated switches and all must be activated
        return (numActivated >= 2 && numActivated == observing.size());
    }
    
    public String logicString() {
        return "and";
    }
    
}
