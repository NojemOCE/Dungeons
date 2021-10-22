package dungeonmania.collectable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity {
    public Armour(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    public void defend() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "armour", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "armour");
    }
}
