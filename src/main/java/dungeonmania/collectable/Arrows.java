package dungeonmania.collectable;

public class Arrows extends CollectableEntity {

    /**
     * Constructor for arrow taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of arrow
     * @param y y coordinate of arrow
     * @param itemId unique item id of arrow
     */
    public Arrows(int x, int y, String itemId) {
        super(x, y, itemId, "arrow");

    }

    /**
     * Constructor for arrow taking its unique itemID and durability
     * @param itemId unique item id of arrow
     * @param durability integer value of durability
     */
    public Arrows(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }
    
    /**
     * Constructor for arrow taking an x and a y coordinate, its unique itemID and durability
     * @param x x coordinate of arrow
     * @param y y coordinate of arrow
     * @param itemId unique item id of arrow
     * @param durability integer value of durability
     */
    public Arrows(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
