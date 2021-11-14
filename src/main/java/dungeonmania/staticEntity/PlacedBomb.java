package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.util.Position;

public class PlacedBomb extends StaticEntity implements Logic {
    private static int BLAST_RADIUS = 1;
    private LogicComponent logic;

    /**
     * Constructor for placed bomb 
     * @param x x coordinate of bomb
     * @param y y coordinate of bomb
     * @param id id of bomb
     * @param logic logic of the bomb
     */

    public PlacedBomb(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "bomb_placed");
        this.logic = logic;
    }

    // Placed bombs act like walls, they cannot be walked over
    @Override
    public Position interact(World world, Entity entity) {
        return entity.getPosition();
    }

    /**
     * Detonates the bomb and destroys entities in radius.
     * Also deals with logic components as required.
     * @param world current world
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
            notifyLogicComponents(world.getStaticEntitiesAtPosition(p));
            world.detonate(p);
        }
    }

    /**
     * Notifies observing logic components of entities to stop observing.
     * @param entities List of entities to check and notify for.
     */
    private void notifyLogicComponents(List<StaticEntity> entities) {
        for (StaticEntity se : entities) {
            if (se instanceof Logic) {
                // null status indicates item to be destroyed
                ((Logic) se).notifyObservers(null);
            }
        }
    }

    @Override
    public LogicComponent getLogic() {
        return logic;
    }

    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("logic", logic.logicString());
		return save;
	}
}
