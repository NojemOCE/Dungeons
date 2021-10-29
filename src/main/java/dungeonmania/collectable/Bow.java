package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.ItemResponse;

public class Bow extends CollectableEntity implements Consumable {

    private final int DURABILITY = 10;
    private final int ATTACK_MULTIPLIER = 2;

    public Bow(int x, int y, String itemId) {
        super(x, y, itemId, "bow");
        setDurability(DURABILITY);
    }

    public void consume() {
        decreaseDurability();
    }

    public int attackModifier() {    
        return ATTACK_MULTIPLIER; 
    }

