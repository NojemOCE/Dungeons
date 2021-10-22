package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Consumable{
    public Sword(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }
    private double attackPower;

    public void consume() {};
    public void attack() {};
}
