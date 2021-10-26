package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Consumable{

    private String itemId;
    private String type = "sword";
    private Inventory inventory;
    private final double ATTACK_POWER = 10;
    private int durability;
    private final int DURABILITY = 10;

    public Sword(Position position, String itemId, Inventory inventory) {
        super(position);
        this.itemId = itemId;
        this.inventory = inventory;
        this.durability = DURABILITY;
    }

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            inventory.removeItem(itemId);
        }
    };

    public double attackModifier() {
        return ATTACK_POWER;
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
