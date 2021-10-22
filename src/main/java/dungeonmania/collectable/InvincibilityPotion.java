package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements Consumable {
    public InvincibilityPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    private int duration;

    public void consume() {};

    public void tick() {};

    public void invincibility() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "invincibility_potion", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "invincibility_potion");
    }
}
