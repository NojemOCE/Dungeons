package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.inventory.Inventory;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements Consumable {

    private String itemId;
    private String type = "bomb";
    private Inventory inventory;

    public Bomb(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    public void consume() {
        inventory.removeItem(itemId);
        drop();
    }

    public void detonate() {
        int bRadius = 1;
        Position current = getPosition();
        for (int i = current.getX() - bRadius; i <= current.getX() + 1; i++) {
            for (int j = current.getY() - bRadius; j <= current.getY() + 1; j++) {

            }
        }
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