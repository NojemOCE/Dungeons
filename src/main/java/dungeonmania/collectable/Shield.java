package dungeonmania.collectable;

public class Shield extends CollectableEntity implements Protection {

    private final int DURABILITY = 10;
    private final int DEFENCE = 10;

    /**
     * Constructor for shield taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of shield
     * @param y y coordinate of shield
     * @param itemId unique item id of shield
     */
    public Shield(int x, int y, String itemId) {
        super(x, y, itemId, "shield");
        setDurability(DURABILITY);
    }

    /**
     * Constructor for shield taking its unique itemID and durability
     * @param itemId unique item id of shield
     * @param durability integer value of durability
     */
    public Shield(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for shield taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of shield
     * @param y y coordinate of shield
     * @param itemId unique item id of shield
     * @param durability integer value of durability
     */
    public Shield(int x, int y, String itemId, int durability) {
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

}
