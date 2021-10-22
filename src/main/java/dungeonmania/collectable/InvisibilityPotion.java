package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Consumable {
    public InvisibilityPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    private int duration;

    public void consume() {};

    public void tick() {};

    public void invisibility() {};
}
