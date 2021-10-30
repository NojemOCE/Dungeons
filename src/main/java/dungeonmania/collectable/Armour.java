package dungeonmania.collectable;

public class Armour extends CollectableEntity {

    private final int DURABILITY = 7;

    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(int x, int y, String itemId) {
        super(x, y, itemId, "armour");
        setDurability(DURABILITY);;
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    }

}
