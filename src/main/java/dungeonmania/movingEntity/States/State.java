package dungeonmania.movingEntity.States;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;

public interface State {
    abstract public void move(MovingEntity e, World world);
    abstract public void move(Boulder e, World world);

    abstract public JSONObject getStateJson();
}
