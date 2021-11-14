package dungeonmania.collectable;

public class SunStone extends CollectableEntity {
    /**
     * Constructor for sun stone taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sun stone
     * @param y y coordinate of sun stone
     * @param itemId unique item id of sun stone
     */
    public SunStone(int x, int y, String itemId) {
        super(x, y, itemId, "sun_stone");
    }
    /**
     * Constructor for sun stone taking its unique itemID and durability
     * @param itemId unique item id of sun stone
     * @param durability integer value of durability
     */
    public SunStone(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    /**
     * Constructor for sun stone taking an x and a y coordinate and its unique itemID
     * @param x x coordinate of sun stone
     * @param y y coordinate of sun stone
     * @param itemId unique item id of sun stone
     * @param durability integer value of durability
     */
    public SunStone(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    @Override
    public CollectableEntity consume() {return null;}
}
