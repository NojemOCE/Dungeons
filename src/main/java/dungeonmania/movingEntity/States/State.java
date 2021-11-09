package dungeonmania.movingEntity.States;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.util.Position;

public interface State {
    abstract public void move(MovingEntity e, World world);
    abstract public Position move(Boulder e, Player p, World world);
}
