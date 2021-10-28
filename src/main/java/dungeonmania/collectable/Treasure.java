package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Treasure extends CollectableEntity implements Consumable {

    public Treasure(int x, int y, String itemId, Inventory inventory) {
        super(x, y, itemId, "treasure", inventory);
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

}
