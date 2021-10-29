package dungeonmania.collectable;

import org.json.JSONObject;

import dungeonmania.inventory.Inventory;

public class Armour extends CollectableEntity {

    private String itemId;
    private final int DURABILITY = 7;

    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(int x, int y, String itemId) {
        super(x, y, itemId, "armour");
        this.itemId = itemId;
        setDurability(DURABILITY);;
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    }

    public void consume() {
        decreaseDurability();  
    };

}
