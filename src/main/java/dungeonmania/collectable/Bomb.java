package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements Consumable {

    private World world;

    public Bomb(Position position, String itemId, World world) {
        super(position, itemId, "bomb");
        this.world = world;
    }

    public void consume() {
        getInventory().removeItem(getItemId());
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

}