package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity implements Consumable {
    public HealthPotion(int x, int y, String id) {
        super(new Position(x, y, 1), id, "health_potion");
    }
    private double healingAmount;
    public void consume() {};
    public void heal() {};

}
