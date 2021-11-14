package dungeonmania.collectable;

public class MidnightArmour extends CollectableEntity implements Protection, Weapon {
    
    private final int DEFENCE = 12;
    private final double ATTACK = 2;

    public MidnightArmour(int x, int y, String itemId) {
        super(x, y, itemId, "midnight_armour");
    }

    public MidnightArmour(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

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

