package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Wood extends CollectableEntity implements Consumable {
    public Wood(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

public class Wood extends CollectableEntities implements Consumable {
    public Wood() {};
    public void consume() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "wood", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "wood");
    }
}
