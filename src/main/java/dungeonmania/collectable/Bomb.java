package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;

public class Bomb extends CollectableEntity implements Consumable {

    private World world;

    public Bomb(int x, int y, String itemId, World world, Inventory inventory) {
        super(x, y, itemId, "bomb", inventory);
        this.world = world;
    }

    public void consume() {
        getInventory().removeItem(getId());
        drop();

    }

}
