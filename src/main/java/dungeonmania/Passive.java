package dungeonmania;

public interface Passive {
    public void decreaseDuration();

    public int getDuration();

    public String getType();

    public void applyPassive();
}
