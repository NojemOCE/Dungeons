package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {
    public Key(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    // need to change to variable type Door
    private Door door;

    public Key() {};

    public void consume() {};

    public void craft() {};

    public void unlock() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "key", getPosition(), false);
    }
    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "key");
    }
}
