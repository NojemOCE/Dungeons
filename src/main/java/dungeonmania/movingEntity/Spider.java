package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.CircleMovement;
import dungeonmania.util.*;


public class Spider extends MovingEntity {


    public Spider(int x, int y, String id) {
        //Attack damage to 1 and layer to 2 for now
        super(new Position(x,y,2), id, "spider", new HealthPoint(100), 1);
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



}
