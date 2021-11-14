package dungeonmania.collectable;

public class Treasure extends CollectableEntity {

    /**
     * Constructor for treasure taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of treasure
     * @param y y coordinate of treasure
     * @param itemId unique item id of treasure
     */
    public Treasure(int x, int y, String itemId) {
        super(x, y, itemId, "treasure");
    }

    /**
     * Constructor for treasure taking its unique itemID and durability
     * @param itemId unique item id of treasure
     * @param durability integer value of durability
     */
    public Treasure(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for treasure taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of treasure
     * @param y y coordinate of treasure
     * @param itemId unique item id of treasure
     * @param durability integer value of durability
     */
    public Treasure(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
