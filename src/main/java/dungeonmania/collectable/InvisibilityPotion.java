package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Consumable {
    public InvisibilityPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    private int duration;

    public InvisibilityPotion() {};

    public void consume() {};

    public void tick() {};

    public void invisibility() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "invisibility_potion", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "invisibility_potion");
    }
}
