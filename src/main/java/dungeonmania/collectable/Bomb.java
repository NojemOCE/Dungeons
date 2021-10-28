package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.World;
import dungeonmania.inventory.Inventory;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends CollectableEntity implements Consumable {

    private World world;

    public Bomb(Position position, String itemId, World world, Inventory inventory) {
        super(position, itemId, "bomb", inventory);
        this.world = world;
    }

    public void consume() {
        getInventory().removeItem(getId());
        drop();
    }

    // public List<Position> detonate() {
    //     int bRadius = 1;
    //     List<Position> blasted = new ArrayList<>();
    //     Position current = getPosition();
    //     for (int i = current.getX() - bRadius; i <= current.getX() + 1; i++) {
    //         for (int j = current.getY() - bRadius; j <= current.getY() + 1; j++) {
    //             if (world.inBounds(i,j)) {
    //                 blasted.add(new Position(i, j));
    //             }
    //         }
    //     }

    //     return blasted;
    // }

}
