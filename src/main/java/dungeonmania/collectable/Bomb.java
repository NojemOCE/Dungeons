package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements Consumable {
    public Bomb(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }
    public void consume() {};
    public void detonate() {};
}
