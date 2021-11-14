package dungeonmania.collectable;

public class Anduril extends CollectableEntity implements Weapon {

    private final double ATTACK_POWER = 5;

    /**
     * Constructor for anduril taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of anduril
     * @param y y coordinate of anduril
     * @param itemId unique item id of anduril
     */
    public Anduril(int x, int y, String itemId) {
        super(x, y, itemId, "anduril");
    }

    /**
     * Constructor for anduril taking its unique itemID and durability
     * @param itemId unique item id of anduril
     * @param durability integer value of durability
     */
    public Anduril(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for anduril taking an x and a y coordinate, its unique itemID and its durability
     * @param x x coordinate of anduril
     * @param y y coordinate of anduril
     * @param itemId unique item id of anduril
     * @param durability integer value of durability
     */
    public Anduril(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * Provides the attack modifier of anduril in normal battle
     * @return ATTACK_POWER
     */
    public double attackModifier() {
        return ATTACK_POWER;
    }

    /**
     * Provides the attack modifier of anduril in boss battle
     * @return ATTACK_POWER * 3
     */
    public double bossAttackModifier() {
        return 3 * attackModifier();
    }

    @Override
    public CollectableEntity consume() {return null;};
}

