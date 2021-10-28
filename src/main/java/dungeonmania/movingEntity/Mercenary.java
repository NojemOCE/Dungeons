package dungeonmania.movingEntity;

import dungeonmania.util.*;
import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.response.models.EntityResponse;


public class Mercenary extends MovingEntity {

    private double BATTLE_RADIUS = 5;
    private Player subject;

    /**
     * Constructor for Mercenary taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id unique entity id of the mercenary
     */
    public Mercenary(int x, int y, String id) {
        super(new Position(x, y), id, "mercenary", new HealthPoint(9), 10);
        setMovement(new FollowPlayer());
        setDefaultMovementStrategy(new FollowPlayer());
        setAlly(false);
    }


    @Override
    public void move(World world) {

        Position distance = Position.calculatePositionBetween(world.getPlayer().getPosition(), this.getPosition());
        double x = (double)distance.getX();
        double y = (double)distance.getY();
        double distanceSquared = ((x*x) + (y*y));
        if ((Math.pow(distanceSquared, 1/2)) <= BATTLE_RADIUS) {
            // mount player as in range
            world.getPlayer().registerEntity(this);
            this.subject = world.getPlayer();
        } else {
            this.subject = null;
            world.getPlayer().unregisterEntity(this);
        }
        
        getMovement().move(this, world);
    }

    // TODO this needs to be updated
    public void update(Movement movement) {
        // take in duration left,
        //after duration is 0 revert back to normal pattern;
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), true);
    }
}
