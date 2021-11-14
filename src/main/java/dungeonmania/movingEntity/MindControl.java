package dungeonmania.movingEntity;

public interface MindControl {

    /**
     * Updates whether the mercenary component is an ally depending on the remaining effect duration
     * Used for mind control
     * @param effectDuration duration of the mind control effect
     */
    public void updateDuration(int effectDuration);
}
