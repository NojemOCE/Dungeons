package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements Consumable {
    public Bomb(int x, int y, String id) {
        super(new Position(x, y, 0), id, "bomb");
    }
    public void consume() {};
    public void detonate() {};

}
