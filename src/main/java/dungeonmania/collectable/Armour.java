package dungeonmania.collectable;

import org.json.JSONObject;

import dungeonmania.inventory.Inventory;

public class Armour extends CollectableEntity {

    private String itemId;
    private Inventory inventory;
    private int durability;
    private final int DURABILITY = 7;

    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(int x, int y, String itemId, Inventory inventory) {
        super(x, y, itemId, "armour", inventory);
        this.itemId = itemId;
        this.inventory = inventory;
        this.durability = DURABILITY;
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    }

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            inventory.removeItem(itemId);
        }
    }

    // Note: saveJSON method created in super class

}
