package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Treasure extends CollectableEntity {

    public Treasure(int x, int y, String itemId) {
        super(x, y, itemId, "treasure");
    }
}
