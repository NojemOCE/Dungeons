package dungeonmania.movingEntity;

public interface PlayerPassiveObserver {
    public String getId();
    public String getType();

    /**
     * Updates the moving entity's movement strategy depending on the passive string passed in
     * @param passive passive string that determines the moving entity's movement strategy
     */
    public void updateMovement(String passive);
}
