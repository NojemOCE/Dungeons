package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
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
        super(new Position(x, y, 2), id, "spider", new HealthPoint(SPIDER_HEALTH), SPIDER_ATTACK);
        setMovement(new CircleMovement());
        setDefaultMovementStrategy(new CircleMovement());
        setAlly(false);
        // Then when character is invincible, udate defaultMovement to currentMovement, and set currentMovement to runAway.
        // When character is no longer invincible, set currentMovement = defaultMovement
    }


    @Override
    /**
     * Simulates 1 ticks worth of movement for the spider. Note: if the spiders
     * movement is obstructed, then its direction will reverse on this tick,
     * and movement will recommence the following tick
     * @param world World in which the spider is a character of
     */
    public void move(World world) {
        getMovement().move(this, world);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject spiderJSON = super.saveGameJson();
        JSONObject movement = new JSONObject();

        movement.put("default-movement", defaultMovementStrategy.getMovementType());
        movement.put("movement-strategy", movementStrategy.getMovementType());

        if(defaultMovementStrategy instanceof CircleMovement) {
            CircleMovement moveStrat = (CircleMovement) defaultMovementStrategy;
            movement.put("current-direction", moveStrat.getCurrentDirection());
            movement.put("next-direction", moveStrat.getNextDirection());
            movement.put("remMovesCurr", moveStrat.getRemMovesCurr());
            movement.put("remMovesNext", moveStrat.getRemMovesNext());
        }
        
        spiderJSON.put("movement", movement);

        return spiderJSON;
    }


    @Override
    public void updateMovement(String passive) {
        if (passive.equals("invincibility_potion")) {
            setMovement(new RunAway());
        } else {
            setMovement(getDefaultMovementStrategy());
        }
    }



}
