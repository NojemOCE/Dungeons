package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.util.*;

import org.json.JSONObject;

import dungeonmania.*;

public interface MovementStrategy {

    /**
     * Takes a moving entity and the current game world, and moves them to their next position
     * @param me entity to move
     * @param world current game world
     */
    public void move(MovingEntity me, World world);

    /**
     * return movement type
     * @return
     */
    public String getMovementType();

    public JSONObject getMovementJson();
}
