package dungeonmania.collectable;

public class Armour extends CollectableEntity {

    private final int DURABILITY = 7;
    private final double DEFENCE_MULTIPLIER = 0.5;

    /**
     * Constructor for armour taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of armour
     * @param y y coordinate of armour
     * @param itemId unique item id of armour
     */
    public Armour(int x, int y, String itemId) {
        super(x, y, itemId, "armour");
        setDurability(DURABILITY);;
    }

    /**
     * Constructor for armour taking its unique itemID and durability
     * @param itemId unique item id of armour
     * @param durability integer value of durability
     */
    public Armour(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for armour taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of armour
     * @param y y coordinate of armour
     * @param itemId unique item id of armour
     * @param durability integer value of durability
     */
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
