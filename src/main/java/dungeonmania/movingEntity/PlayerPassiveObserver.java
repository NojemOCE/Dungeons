package dungeonmania.movingEntity;

public interface PlayerPassiveObserver {
    /**
     * Gets the id of the PlayerPassiveObserver
     * @return PlayerPassiveObserver unique id
     */
    public String getId();

    /**
     * Gets the type of the PlayerPassiveObserver
     * @return PlayerPassiveObserver string type
     */
    public String getType();

    /**
     * Updates the moving entity's movement strategy depending on the passive string passed in
     * @param passive passive string that determines the moving entity's movement strategy
     */
    public void updateMovement(String passive);
}
