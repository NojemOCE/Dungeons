package dungeonmania.collectable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity {
    public Armour(int x, int y, String id) {
        super(new Position(x, y, 1), id, "armour");
    }

    public void defend() {};

}
