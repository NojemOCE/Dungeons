package dungeonmania.movingEntity.States;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.util.Position;


public class SwampState implements State {
    private int remTicks;

    public SwampState(int remTicks) {
        this.remTicks = remTicks - 1;
    }

    @Override
    public void move(MovingEntity e, World world) {
        remTicks--;

        if (remTicks == 0)  {
            e.setState(new NormalState());
        }
        
    }

    @Override
    public Position move(Boulder e, Player p, World world) {
        remTicks--;

        if (remTicks == 0)  {
            e.setState(new NormalState());
        }
        return e.getPosition();        
    }


}
