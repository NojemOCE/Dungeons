package dungeonmania.collectable;

public class MidnightArmour extends CollectableEntity implements Protection, Weapon {
    
    private final int DEFENCE = 10;
    private final double ATTACK = 5;

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
     * provides the defence modifier of shield
     * @return DEFENCE
     */
    public int defenceModifier() { 
        return DEFENCE;
    }

    public double attackModifier() {
        return ATTACK;
    }

    public double bossAttackModifier() {
        return attackModifier();
    }

    @Override
    public CollectableEntity consume() {return null;};
}

