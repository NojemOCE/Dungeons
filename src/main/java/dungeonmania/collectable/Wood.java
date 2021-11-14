package dungeonmania.collectable;

public class Wood extends CollectableEntity {

    /**
     * Constructor for wood taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of wood
     * @param y y coordinate of wood
     * @param itemId unique item id of wood
     */
    public Wood(int x, int y, String itemId) {
        super(x, y, itemId, "wood");
    }

    /**
     * Constructor for wood taking its unique itemID and durability
     * @param itemId unique item id of wood
     * @param durability integer value of durability
     */
    public Wood(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for wood taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of wood
     * @param y y coordinate of wood
     * @param itemId unique item id of wood
     * @param durability integer value of durability
     */
    public Wood(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }
}
