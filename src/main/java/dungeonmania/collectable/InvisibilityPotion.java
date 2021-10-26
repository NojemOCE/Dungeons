package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Consumable {
    public InvisibilityPotion(int x, int y, String id) {
        super(new Position(x, y, 1), id, "invisibility_potion");
    }

    private int duration;

    public InvisibilityPotion() {};

    public void consume() {};

    public void tick() {};

    public void invisibility() {};

}
