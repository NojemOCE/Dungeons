package dungeonmania.movingEntity.States;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;

public interface State {

    /**
     * Moves a given moving entity in accordance with its movement strategy
     * @param e moving entity to move
     * @param world world that the moving entity resides in. Allows the entity to observe its surroundings
     */
    abstract public void move(MovingEntity e, World world);

    /**
     * Moves a given boulder
     * @param e boulder to move
     * @param world world that the boulder resides in. Allows the boulder to observe its surroundings
     */
    abstract public void move(Boulder e, World world);

    /**
     * Creates and returns a JSON object storing the current movement state details
     * @return JSON Object for state
     */
    abstract public JSONObject getStateJson();
}
