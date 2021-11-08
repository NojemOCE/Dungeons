package dungeonmania.collectable;

public class Arrows extends CollectableEntity {

    public Arrows(int x, int y, String itemId) {
        super(x, y, itemId, "arrow");

    }

    public Arrows(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }
    
    public Arrows(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

}
