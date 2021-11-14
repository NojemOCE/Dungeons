package dungeonmania.collectable;

public class OneRing extends CollectableEntity {

    /**
     * Constructor for one ring taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of one ring
     * @param y y coordinate of one ring
     * @param itemId unique item id of one ring
     */
    public OneRing(int x, int y, String itemId) {
        super(x, y, itemId, "one_ring");
    }

    /**
     * Constructor for one ring taking its unique itemID and durability
     * @param itemId unique item id of one ring
     * @param durability integer value of durability
     */
    public OneRing(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for one ring taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of one ring
     * @param y y coordinate of one ring
     * @param itemId unique item id of one ring
     * @param durability integer value of durability
     */
    public OneRing(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
