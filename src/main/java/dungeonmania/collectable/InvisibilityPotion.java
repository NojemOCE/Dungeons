package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Consumable {

    private World world;
    private int duration;
    private final int DURATION = 10;
    private boolean active = false;

    public InvisibilityPotion(int x, int y, String itemId, World world, Inventory inventory) {
        super(x, y, itemId, "invisibility_potion", inventory);
        this.world = world;
        this.duration = DURATION;
    }


    public void consume() {
        this.active = true;
        // notify the world that the invisibility potion effect is activated
        world.update(getType());
    };

    @Override
    public void tick() {
        this.duration--;
        if (this.duration == 0) {
            getInventory().removeItem(getId());
            // notify the world that the invisibility potion effect is over
            world.update(getType());
        }

    }

}
