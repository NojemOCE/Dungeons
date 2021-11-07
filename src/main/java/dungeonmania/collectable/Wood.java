package dungeonmania.collectable;

public class Wood extends CollectableEntity {

    public Wood(int x, int y, String itemId) {
        super(x, y, itemId, "wood");
    }

    public Wood(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Wood(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }
}
