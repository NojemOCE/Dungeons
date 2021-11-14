package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.movingEntity.States.State;
import dungeonmania.util.*;


public class Spider extends MovingEntity implements PlayerPassiveObserver {

    static final int SPIDER_ATTACK = 5;
    static final int SPIDER_HEALTH = 5;
    /**
     * Constructor for spider taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the spider
     * @param y y coordinate of the spider
     * @param id unique entity id of the spider
     */
    public Spider(int x, int y, String id) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "spider", new HealthPoint(SPIDER_HEALTH), SPIDER_ATTACK);
        setMovement(new CircleMovement());
        setDefaultMovementStrategy(new CircleMovement());
        setAlly(false);
    }

    /**
     * Constructor for spider taking x, y coordinates, id, Healthpoint, default movement strategy, current movement strategy, and the state of the spider
     * @param x x coordinate of the spider
     * @param y y coordinate of the spider
     * @param id iunique entity id of the spider
     * @param hp healthpoint object of the spider
     * @param defaultMovement default movement strategy of the spider
     * @param currentMovement current movement strategy of the spider
     * @param state state of the spider's movement (normal or swamp)
     */
    public Spider(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement, MovementStrategy currentMovement, State state) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "spider", hp, SPIDER_ATTACK);
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
       checkSwampTile(world);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject spiderJSON = super.saveGameJson();

        spiderJSON.put("default-strategy", defaultMovementStrategy.getMovementJson());
        spiderJSON.put("movement-strategy", movementStrategy.getMovementJson());
        spiderJSON.put("state", getState().getStateJson());

        return spiderJSON;
    }

    @Override
    public void updateMovement(String passive) {
        if (movementStrategy instanceof CircleMovement) {
            CircleMovement moveStrat = (CircleMovement) movementStrategy;

            if (passive.equals("invincibility_potion")) {
                moveStrat.setAvoidPlayer(true);
            }
            else {
                moveStrat.setAvoidPlayer(false);
            }
        }
    }


}
