package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Wood extends CollectableEntity implements Consumable {
    public Wood(int x, int y, String id) {
        super(new Position(x, y, 1), id, "wood");
    }

public class Wood extends CollectableEntities implements Consumable {
    public Wood() {};
    public void consume() {};

}
