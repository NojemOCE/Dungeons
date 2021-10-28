package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Position;

public class PlacedBomb extends StaticEntity {
    private int BLAST_RADIUS = 1;

    public PlacedBomb(int x, int y, String id) {
        super(new Position(x, y, 1), id, "bomb");
    }

    // Placed bombs act like walls, they cannot be walked over
    @Override
    public Position interact(World world, Entity entity) {
        return entity.getPosition();
    }

    public void detonate(World world) {
        List<Position> toDetonate = new ArrayList<>();
        Position current = getPosition();
        for (int i = current.getX() - BLAST_RADIUS; i <= current.getX() + 1; i++) {
            for (int j = current.getY() - BLAST_RADIUS; j <= current.getY() + 1; j++) {
                if (i == 0 || i == world.getHighestX() || j == 0 || j == world.getHighestY()) {
                    // do not detonate walls
                } else if (world.inBounds(i,j)) {
                    toDetonate.add(new Position(i, j));
                }
            }
        }    
        
        for (Position p : toDetonate) {
            world.detonate(p);
        }
    }
}
