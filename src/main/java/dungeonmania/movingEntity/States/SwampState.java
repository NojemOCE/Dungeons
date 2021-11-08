package dungeonmania.movingEntity.States;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;

public class SwampState implements State {
    private int remTicks;

    public SwampState(int remTicks) {
        this.remTicks = remTicks;
    }

    @Override
    public void move(MovingEntity e, World world) {
        remTicks--;

        if (remTicks == 0)  {
            e.setState(new NormalState());
        }
        
    }

    @Override
    public void move(Boulder e, World world) {
        // TODO Auto-generated method stub
        
    }


}
