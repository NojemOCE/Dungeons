package dungeonmania.collectable;

import dungeonmania.Weapon;

public class Anduril extends CollectableEntity implements Weapon {

    private final double ATTACK_POWER = 3;
    private final int DURABILITY = 10;

    public Anduril(int x, int y, String itemId) {
        super(x, y, itemId, "anduril");
        setDurability(DURABILITY);
    }

    public Anduril(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Anduril(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * Provides the attack modifier of the sword
     * @return ATTACK_POWER
     */
    public double attackModifier() {
        return ATTACK_POWER;
    }

    public double bossAttackModifier() {
        return 3 * attackModifier();
    }

}

