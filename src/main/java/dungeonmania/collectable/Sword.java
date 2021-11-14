package dungeonmania.collectable;

public class Sword extends CollectableEntity implements Weapon {

    private final double ATTACK_POWER = 3;
    private final int DURABILITY = 10;

    /**
     * Constructor for sword taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sword
     * @param y y coordinate of sword
     * @param itemId unique item id of sword
     */
    public Sword(int x, int y, String itemId) {
        super(x, y, itemId, "sword");
        setDurability(DURABILITY);
    }

    /**
     * Constructor for sword taking its unique itemID and durability
     * @param itemId unique item id of sword
     * @param durability integer value of durability
     */
    public Sword(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for sword taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sword
     * @param y y coordinate of sword
     * @param itemId unique item id of sword
     * @param durability integer value of durability
     */
    public Sword(int x, int y, String itemId, int durability) {
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
        return attackModifier();
    }

}
