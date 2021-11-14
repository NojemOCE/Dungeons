package dungeonmania;

import dungeonmania.movingEntity.Player;

public interface Passive {
    /**
     * Decreases the duration of the Passive
     */
    public void decreaseDuration();

    /**
     * Gets the duration of the passive
     * @return passive duration
     */
    public int getDuration();

    /**
     * Gets the string type of the passive
     * @return string type of passive
     */
    public String getType();

    /**
     * Applies the effects of the passive to the provided player
     * @param player current games player
     */
    public void applyPassive(Player player);
}
