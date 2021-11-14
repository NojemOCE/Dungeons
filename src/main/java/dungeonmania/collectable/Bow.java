package dungeonmania.collectable;


public class Bow extends CollectableEntity {

    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    /**
     * Constructor for bow taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of bow
     * @param y y coordinate of bow
     * @param itemId unique item id of bow
     */
    public Bow(int x, int y, String itemId) {
        super(x, y, itemId, "bow");
        setDurability(DURABILITY);
    }

    /**
     * Constructor for bow taking its unique itemID and durability
     * @param itemId unique item id of bow
     * @param durability integer value of durability
     */
    public Bow(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for bow taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of bow
     * @param y y coordinate of bow
     * @param itemId unique item id of bow
     * @param durability integer value of durability
     */
    public Bow(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * Provides the attack modifier of the bow
     * @return ATTACK_MULTIPLIER
     */
    public int attackModifier() {    
        return ATTACK_MULTIPLIER; 
    }

}

