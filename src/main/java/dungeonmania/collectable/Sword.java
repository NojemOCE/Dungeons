package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Consumable{
    public Sword(int x, int y, String id) {
        super(new Position(x, y, 1), id, "sword");
    }

    private double attackPower;

    public void consume() {};
    public void attack() {};

}
