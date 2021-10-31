package dungeonmania.collectable;

public class Bomb extends CollectableEntity {

    public Bomb(int x, int y, String itemId) {
        super(x, y, itemId, "bomb");
    }

    public Bomb(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Bomb(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }
}
