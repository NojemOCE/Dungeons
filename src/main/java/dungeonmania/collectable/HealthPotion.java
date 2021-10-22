package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity implements Consumable {
    public HealthPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }
    private double healingAmount;
    public void consume() {};
    public void heal() {};
}
