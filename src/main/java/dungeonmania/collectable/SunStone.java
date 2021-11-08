package dungeonmania.collectable;

public class SunStone extends CollectableEntity {
    public SunStone(int x, int y, String itemId) {
        super(x, y, itemId, "sun_stone");
    }

    public SunStone(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public SunStone(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    @Override
    public CollectableEntity consume() {return null;}
}
