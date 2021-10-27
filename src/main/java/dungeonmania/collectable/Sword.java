package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Consumable{

    private final double ATTACK_POWER = 10;
    private int durability;
    private final int DURABILITY = 10;

    public Sword(Position position, String itemId) {
        super(position, itemId, "sword");
        this.durability = DURABILITY;
    }

    public void consume() {
        this.durability--;
        if (this.durability == 0) {
            getInventory().removeItem(getItemId());
        }
    };

    public double attackModifier() {
        return ATTACK_POWER;
    };
    //public Sword() {};
    public void attack() {};

}
