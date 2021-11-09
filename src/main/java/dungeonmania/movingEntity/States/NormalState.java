package dungeonmania.movingEntity.States;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;

public class NormalState implements State{

    
    public NormalState() {
    }

    @Override
    public void move(MovingEntity e, World world) {
        e.moveEntity(world);
        
    }

    @Override
    public void move(Boulder e, World world) {
        // TODO Auto-generated method stub
        
    }
    
}
