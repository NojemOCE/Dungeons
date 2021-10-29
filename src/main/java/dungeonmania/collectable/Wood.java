package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;

public class Wood extends CollectableEntity implements Consumable {

    public Wood(int x, int y, String itemId, Inventory inventory) {
        super(x, y, itemId, "wood", inventory);
    }

    public void consume() {
        getInventory().removeItem(getId());
    }

}
