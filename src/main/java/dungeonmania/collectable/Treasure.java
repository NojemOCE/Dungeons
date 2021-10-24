package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity implements Consumable {
    public Treasure(int x, int y, String id) {
        super(new Position(x, y, 0), id, "treasure");
    }

    public void consume() {};

}
