package dungeonmania.movingEntity.States;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.util.Position;


public class SwampState implements State {
    private int remTicks;

    /**
     * Constructor for swamp movement state taking remaining ticks on swamp tile
     * @param remTicks remaining ticks on swamp tile
     */
    public SwampState(int remTicks) {
        this.remTicks = remTicks - 1;
    }

    @Override
    public void move(MovingEntity e, World world) {
        if (remTicks == 0)  {
            e.setState(new NormalState());
            e.move(world);
            return;
        }

        remTicks--;

        
    }

    @Override
    public Position move(Boulder e, Player p, World world) {
        if (remTicks == 0)  {
            e.setState(new NormalState());
            return e.move(p);
        }

        remTicks--;
        
        return e.getPosition();        
    }

    @Override
    public JSONObject getStateJson() {
        JSONObject obj = new JSONObject();
        obj.put("state", "swampState");
        obj.put("remTicks", remTicks+1);
        return obj;
    }


}
