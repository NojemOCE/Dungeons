package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {


    private Door door;

    public Key(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    public Key() {};

    public void consume() {

    }

    public void craft() {
        inventory.removeItem(itemId);
    }

    public void unlock() {
        inventory.removeItem(itemId);
        door.open();
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(itemId, type, getPosition(), false);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(itemId, type);
    }
}
