package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.util.*;


public class Zombie extends MovingEntity{

    /**
     * Constructor for zombie taking an x coordinate, a y coordinate and an id
     * @param x x coordinate of zombie
     * @param y y coordinate of zombie
     * @param id unique entity id of zombie
     */
    public Zombie(int x, int y, String id) {
        super(new Position(x, y), id, "zombie_toast", new HealthPoint(100), 10);
        setMovement(new RandomMovement());
        setDefaultMovementStrategy(new RandomMovement());
        setAlly(false);
    }
    
    @Override
    public void move(World world) {
       getMovement().move(this, world);
    }
}

