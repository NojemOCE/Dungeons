package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {
    public Key(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    // need to change to variable type Door
    private Door door;

    public void consume() {};

    public void craft() {};

    public void unlock() {};

}
