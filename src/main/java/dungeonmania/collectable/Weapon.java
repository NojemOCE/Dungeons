package dungeonmania.collectable;

public interface Weapon {
    /**
     * Gets the attack modifier of the weapon
     * @return attack modifier of the weapon
     */
    public double attackModifier();

    /**
     * Gets the boss attack modifier of the weapon
     * @return boss attack modifier of the weapon
     */
    public double bossAttackModifier();
}
