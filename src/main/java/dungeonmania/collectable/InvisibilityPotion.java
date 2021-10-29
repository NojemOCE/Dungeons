package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity {

    private final int DURATION = 10;
    private boolean active = false;

    public InvisibilityPotion(int x, int y, String itemId) {
        super(x, y, itemId, "invisibility_potion");
        setDurability(DURATION);
    }

    @Override
    public void consume() {
        this.active = true;
        decreaseDurability();
        // notify the world that the invisibility potion effect is activated
        world.update(getType());
    };

    @Override
    public void tick() {
        decreaseDurability();
        if (getDurability() == 0) {
            // notify the world that the invisibility potion effect is over
            world.update(getType());
        }

    }

}
