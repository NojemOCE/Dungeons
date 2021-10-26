package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Consumable {
    private String itemId;
    private String type = "invincibility_potion";
    private Inventory inventory;
    private World world;
    private int duration;
    private final int DURATION = 10;
    private boolean active = false;

    public InvincibilityPotion(Position position, String itemId, Inventory inventory, World world) {
        super(position);
        this.itemId = itemId;
        this.inventory = inventory;
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

    };

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(itemId, type, getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
