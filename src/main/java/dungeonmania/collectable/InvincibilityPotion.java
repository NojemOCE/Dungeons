package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Consumable {
    public InvincibilityPotion(int x, int y, String id) {
        super(new Position(x, y, 1), id, "invincibility_potion");
    }

    private int duration;



    public void consume() {};

    public void tick() {};

    public void invincibility() {};

}
