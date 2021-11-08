package dungeonmania.collectable;

public class Treasure extends CollectableEntity {

    public Treasure(int x, int y, String itemId) {
        super(x, y, itemId, "treasure");
    }

    public Treasure(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Treasure(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
