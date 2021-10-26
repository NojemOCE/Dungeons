package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class OneRing extends CollectableEntity implements Consumable {
    public OneRing(int x, int y, String id) {
        super(new Position(x, y, 1), id, "one_ring");
    }

public class OneRing extends CollectableEntities implements Consumable {
    public OneRing() {};
    public void consume() {};

}
