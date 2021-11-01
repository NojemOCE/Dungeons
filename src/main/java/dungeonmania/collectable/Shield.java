package dungeonmania.collectable;

public class Shield extends CollectableEntity {

    private final int DURABILITY = 10;
    private final int DEFENCE = 10;

    public Shield(int x, int y, String itemId) {
        super(x, y, itemId, "shield");
        setDurability(DURABILITY);
    }

    public Shield(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Shield(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * provides the defence modifier of armour
     * @return DEFENCE
     */
    public int defenceModifier() { 
        return DEFENCE;
    }

}
