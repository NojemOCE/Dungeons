package dungeonmania.collectable;

public class Shield extends CollectableEntity {

    private final int DURABILITY = 10;
    private final int DEFENCE = 10;

    public Shield(int x, int y, String itemId) {
        super(x, y, itemId, "shield");
        setDurability(DURABILITY);
    }

    public int defenceModifier() { 
        return DEFENCE;
    }

}
