package dungeonmania.movingEntity;

public interface PlayerPassiveObserver {
    public String getId();
    public String getType();
    public void updateMovement(String passive);
}
