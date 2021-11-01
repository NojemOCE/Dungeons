package dungeonmania.collectable;

public class Sword extends CollectableEntity {

    private final double ATTACK_POWER = 3;
    private final int DURABILITY = 10;

    public Sword(int x, int y, String itemId) {
        super(x, y, itemId, "sword");
        setDurability(DURABILITY);
    }

    public Sword(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

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

}
