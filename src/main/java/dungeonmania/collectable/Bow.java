package dungeonmania.collectable;

import java.util.HashMap;
import java.util.Map;

public class Bow extends CollectableEntity {

    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    public Bow(int x, int y, String itemId) {
        super(x, y, itemId, "bow");
        setDurability(DURABILITY);
    }

    public Bow(String itemId, int durability) {
        this(0, 0, itemId, durability);
    }

    public Bow(int x, int y, String itemId, int durability) {
        this(x, y, itemId);
        setDurability(durability);
    }

    /**
     * Provides the attack modifier of the bow
     * @return ATTACK_MULTIPLIER
     */
    public int attackModifier() {    
        return ATTACK_MULTIPLIER; 
    }

}

