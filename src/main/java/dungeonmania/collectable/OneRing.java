package dungeonmania.collectable;

public class OneRing extends CollectableEntity {

    public OneRing(int x, int y, String itemId) {
        super(x, y, itemId, "one_ring");
    }

    public OneRing(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public OneRing(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
