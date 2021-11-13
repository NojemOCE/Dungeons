package dungeonmania.collectable;

public class Armour extends CollectableEntity {

    private final int DURABILITY = 7;
    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(int x, int y, String itemId) {
        super(x, y, itemId, "armour");
        setDurability(DURABILITY);;
    }

    public Armour(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Armour(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }
    
    /**
     * provides the defence modifier of armour
     * @return DEFENCE_MODIFIER
     */
    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    }

}
