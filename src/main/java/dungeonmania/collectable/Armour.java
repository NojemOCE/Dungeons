package dungeonmania.collectable;

import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity {

    private String itemId;
    private String type = "armour";
    private Inventory inventory;
    private int durability;
    private final int DURABILITY = 10;

    private final double DEFENCE_MULTIPLIER = 0.5;

    public Armour(Position position, String itemId, Inventory inventory) {
        super(position, itemId, "armour", inventory);
        this.itemId = itemId;
        this.inventory = inventory;
        this.durability = DURABILITY;
    }

    public double defenceModifier() {
        return DEFENCE_MULTIPLIER;
    };

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            inventory.removeItem(itemId);
        }
    };

}
