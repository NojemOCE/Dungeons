package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.util.*;


public class Zombie extends MovingEntity{
    static final int ZOMBIE_ATTACK = 2;
    static final int ZOMBIE_HEALTH = 6;


    /**
     * Constructor for zombie taking an x coordinate, a y coordinate and an id
     * @param x x coordinate of zombie
     * @param y y coordinate of zombie
     * @param id unique entity id of zombie
     */
    public Zombie(int x, int y, String id) {
        super(new Position(x, y, 2), id, "zombie_toast", new HealthPoint(ZOMBIE_HEALTH), ZOMBIE_ATTACK);
        setMovement(new RandomMovement());
        setDefaultMovementStrategy(new RandomMovement());
        setAlly(false);
    }
    
    @Override
    public void move(World world) {
       getMovement().move(this, world);
    }

    @Override
    public JSONObject saveGameJson() {
        // TODO Auto-generated method stub
        return null;
    }
}

