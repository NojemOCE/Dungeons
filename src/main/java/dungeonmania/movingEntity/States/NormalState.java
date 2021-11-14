package dungeonmania.movingEntity.States;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.util.Position;


public class NormalState implements State{

    /**
     * Constructor for normal movement state
     */
    public NormalState() {
    }

    @Override
    public void move(MovingEntity e, World world) {
        e.moveEntity(world);
        
    }

    @Override
    public Position move(Boulder e, Player p, World world) {
        return e.move(p);
    }

    @Override
    public JSONObject getStateJson() {
        JSONObject obj = new JSONObject();
        obj.put("state", "normalState");
        return obj;
    }
    
}
