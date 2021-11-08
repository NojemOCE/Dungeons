package dungeonmania.movingEntity.States;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;

public interface State {
    abstract public void move(MovingEntity e, World world);
    abstract public void move(Boulder e, World world);
}
