package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity {

    private final double ATTACK_POWER = 3;
    private final int DURABILITY = 10;

    public Sword(int x, int y, String itemId) {
        super(x, y, itemId, "sword");
        setDurability(DURABILITY);
    }

    public double attackModifier() {
        return ATTACK_POWER;
    };

}
