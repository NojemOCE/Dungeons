package dungeonmania.collectable;

public class Bow extends CollectableEntity {

    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    public Bow(int x, int y, String itemId) {
        super(x, y, itemId, "bow");
        setDurability(DURABILITY);
    }

    public int attackModifier() {    
        return ATTACK_MULTIPLIER; 
    }

}

