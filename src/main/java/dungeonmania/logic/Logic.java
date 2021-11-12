package dungeonmania.logic;

public interface Logic {
    /**
     * Get the logic component
     */
    public LogicComponent getLogic();

    /**
     * Checks whether the current component is activated
     * @return true if activated, else false
     */
    default public boolean isActivated() {
        return getLogic().isActivated();
    }

    /**
     * Add an observer to this logic component
     * @param logic the component to add
     */
    default public void addObserver(LogicComponent logic) {
        getLogic().addObserver(logic);
    }

    /**
     * Remove an observer to this logic component
     * @param logic the component to remove
     */
    default public void removeObserver(LogicComponent logic) {
        getLogic().removeObserver(logic);
    }

    /**
     * Notify all observers of current status
     * @param status activation status (true or false), or null for removal
     */
    default public void notifyObservers(Boolean status) {
        getLogic().notifyObservers(status);
    }

    /**
     * Update the observing list for this logic component
     */
    default public void update() {
        LogicComponent logic = getLogic();
        logic.update(logic, isActivated());
    }
}
