package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class OneRing extends CollectableEntity implements Consumable {

    public OneRing(Position position, String itemId) {
        super(position, itemId, "one_ring");
    }

    public void consume() {
        getInventory().removeItem(getItemId());
    }

}
