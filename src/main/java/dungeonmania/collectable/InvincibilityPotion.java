package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Consumable {

    private World world;
    private int duration;
    private final int DURATION = 10;
    private boolean active = false;

    public InvincibilityPotion(Position position, String itemId, World world) {
        super(position, itemId, "invincibility_potion");
        this.world = world;
        this.duration = DURATION;
    }


    public void consume() {
        this.active = true;
        // notify the world that the invisibility potion effect is activated
        world.update(type);
    };

    @Override
    public void tick() {
        this.duration--;
        if (this.duration == 0) {
            inventory.removeItem(itemId);
            // notify the world that the invisibility potion effect is over
            world.update(type);
        }

    }

}
