package dungeonmania.collectable;
import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Consumable{
    public Sword(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }
    private double attackPower;

    public Sword() {};
    public void consume() {};
    public void attack() {};

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "sword", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "sword");
    }
}
