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
     * Gets the movement type as a string
     * @return string for movement strategy type
     */
    public String getMovementType();

    /**
     * Creates and returns a JSON Object for the movement strategy
     * @return JSON object for movement strategy
     */
    public JSONObject getMovementJson();
}
