package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.States.State;
import dungeonmania.util.*;


public class Spider extends MovingEntity implements PlayerPassiveObserver {

    static final int SPIDER_ATTACK = 1;
    static final int SPIDER_HEALTH = 3;
    /**
     * Constructor for spider taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the spider
     * @param y y coordinate of the spider
     * @param id unique entity id of the spider
     */
    public Spider(int x, int y, String id) {
        //Attack damage to 1 and layer to 2 for now
        super(new Position(x, y, Position.MOVING_LAYER), id, "spider", new HealthPoint(SPIDER_HEALTH), SPIDER_ATTACK);
        setMovement(new CircleMovement());
        setDefaultMovementStrategy(new CircleMovement());
        setAlly(false);
        // Then when character is invincible, udate defaultMovement to currentMovement, and set currentMovement to runAway.
        // When character is no longer invincible, set currentMovement = defaultMovement
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
    /**
     * Simulates 1 ticks worth of movement for the spider. Note: if the spiders
     * movement is obstructed, then its direction will reverse on this tick,
     * and movement will recommence the following tick
     * @param world World in which the spider is a character of
     */
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
