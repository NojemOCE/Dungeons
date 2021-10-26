package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity implements Consumable {
    public Treasure(int x, int y, String id) {
        super(new Position(x, y, 1), id, "treasure");
    }

public class Treasure extends CollectableEntities implements Consumable {
    public Treasure() {};
    public void consume() {};

}
