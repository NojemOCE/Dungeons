package dungeonmania;

import dungeonmania.movingEntity.Player;

public interface Passive {
    /**
     * decreases the duration of the Passive
     */
    public void decreaseDuration();

    public int getDuration();

    public String getType();

    /**
     * applies the effects of the passive to the provided player
     */
    public void applyPassive(Player player);
}
