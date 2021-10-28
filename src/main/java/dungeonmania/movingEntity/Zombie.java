package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.util.*;


public class Zombie extends MovingEntity{

    public Zombie(int x, int y, String id) {
        super(new Position(x, y), id, "zombie_toast", new HealthPoint(100), 10);
        setMovement(new RandomMovement());
        setDefaultMovementStrategy(new RandomMovement());
        setAlly(false);
    }
  
    public void move(World world) {
       getMovement().move(this, world);
    }



}

