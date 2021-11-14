package dungeonmania.collectable;

public class MidnightArmour extends CollectableEntity implements Protection, Weapon {
    
    private final int DEFENCE = 12;
    private final double ATTACK = 2;

    /**
     * Constructor for midnight armour taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of midnight armour
     * @param y y coordinate of midnight armour
     * @param itemId unique item id of midnight armour
     */
    public MidnightArmour(int x, int y, String itemId) {
        super(x, y, itemId, "midnight_armour");
    }

    /**
     * Constructor for midnight armour taking its unique itemID and durability
     * @param itemId unique item id of midnight armour
     * @param durability integer value of durability
     */
    public MidnightArmour(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for midnight armour taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of midnight armour
     * @param y y coordinate of midnight armour
     * @param itemId unique item id of midnight armour
     * @param durability integer value of durability
     */
    public MidnightArmour(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * provides the defence modifier of the midnight Armour
     * @return DEFENCE
     */
    public int defenceModifier() { 
        return DEFENCE;
    }

    /**
     * Provides the attack modifier of the midnight armour in normal battle
     * @return ATTACK_POWER
     */
    public double attackModifier() {
        return ATTACK;
    }

    /**
     * Provides the attack modifier of the midnight armour in boss battle
     * @return ATTACK_POWER
     */
    public double bossAttackModifier() {
        return attackModifier();
    }

    @Override
    public CollectableEntity consume() {return null;};
}

