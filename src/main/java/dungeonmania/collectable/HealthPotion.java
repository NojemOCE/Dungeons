package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity {
    public HealthPotion(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }
    private double healingAmount;
<<<<<<< HEAD
    //public void consume() {};
    public double heal() {};
=======

    public HealthPotion() {};
    public void consume() {};
    public void heal() {};
>>>>>>> master

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "health_potion", getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        // TODO Update for valid ID
        return new ItemResponse("not a real ID", "health_potion");
    }
}
