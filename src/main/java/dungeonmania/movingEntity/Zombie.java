package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
import dungeonmania.movingEntity.States.State;
import dungeonmania.util.*;


public class Zombie extends MovingEntity implements PlayerPassiveObserver {
    static final int ZOMBIE_ATTACK = 2;
    static final int ZOMBIE_HEALTH = 6;


    /**
     * Constructor for zombie taking an x coordinate, a y coordinate and an id
     * @param x x coordinate of zombie
     * @param y y coordinate of zombie
     * @param id unique entity id of zombie
     */
    public Zombie(int x, int y, String id) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "zombie_toast", new HealthPoint(ZOMBIE_HEALTH), ZOMBIE_ATTACK);
        setMovement(new RandomMovement());
        setDefaultMovementStrategy(new RandomMovement());
        setAlly(false);
    }

    /**
     * Constructor for zombie taking x, y coordinates, id, Healthpoint, default movement strategy, current movement strategy, and the state of the zombie
     * @param x x coordinate of the zombie
     * @param y y coordinate of the zombie
     * @param id iunique entity id of the zombie
     * @param hp healthpoint object of the zombie
     * @param defaultMovement default movement strategy of the zombie
     * @param currentMovement current movement strategy of the zombie
     * @param state state of the zombie's movement (normal or swamp)
     */
    public Zombie(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement, MovementStrategy currentMovement, State state) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "zombie_toast", hp, ZOMBIE_ATTACK);
        setMovement(currentMovement);
        setDefaultMovementStrategy(defaultMovement);
        setAlly(false);
        setState(state);
    }

    @Override
    public void move(World world) {
       getState().move(this, world);
    }

    @Override
    public void moveEntity(World world) {
       getMovement().move(this, world);
    }

    @Override
    public void updateMovement(String passive) {
        if (passive.equals("invincibility_potion")) {
            setMovement(new RunAway());
        } else {
            setMovement(getDefaultMovementStrategy());
        }
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject zombieJSON = super.saveGameJson();

        zombieJSON.put("default-strategy", defaultMovementStrategy.getMovementJson());
        zombieJSON.put("movement-strategy", movementStrategy.getMovementJson());
        zombieJSON.put("state", getState().getStateJson());

        return zombieJSON;
    }
}

