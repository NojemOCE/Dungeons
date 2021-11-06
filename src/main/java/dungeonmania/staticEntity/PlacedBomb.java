package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Position;

public class PlacedBomb extends StaticEntity {
    private int BLAST_RADIUS = 1;

    /**
     * Constructor for placed bomb 
     * @param x x coordinate of bomb
     * @param y y coordinate of bomb
     * @param id id of bomb
     */
    public PlacedBomb(int x, int y, String id) {
        super(new Position(x, y, 1), id, "placed_bomb");
    }

    // Placed bombs act like walls, they cannot be walked over
    @Override
    public Position interact(World world, Entity entity) {
        return entity.getPosition();
    }

    /**
     * Detonates the bomb and destroys entities in radius
     * @param world
     */
    public void detonate(World world) {
        List<Position> toDetonate = new ArrayList<>();
        Position current = getPosition();
        for (int i = current.getX() - BLAST_RADIUS; i <= current.getX() + BLAST_RADIUS; i++) {
            for (int j = current.getY() - BLAST_RADIUS; j <= current.getY() + BLAST_RADIUS; j++) {
                toDetonate.add(new Position(i, j));
            }
        }    
        
        for (Position p : toDetonate) {
            world.detonate(p);
        }
    }
}
