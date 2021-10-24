package dungeonmania.collectable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Arrows extends CollectableEntity {

    public Arrows(int x, int y, String id) {
        super(new Position(x, y, 1), id, "arrow");
    }

}
