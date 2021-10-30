package dungeonmania.movingEntity;

import dungeonmania.util.*;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.response.models.EntityResponse;


public class Mercenary extends MovingEntity {

    static final int MERC_ATTACK = 3;
    static final int MERC_HEALTH = 9;
    private static final int GOLD_TO_BRIBE = 1;
    private static final double BATTLE_RADIUS = 15;
    private boolean interactable = false;

    /**
     * Constructor for Mercenary taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id unique entity id of the mercenary
     */
    public Mercenary(int x, int y, String id) {
        super(new Position(x, y, 2), id, "mercenary", new HealthPoint(MERC_HEALTH), MERC_ATTACK);
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
        
        if ((Math.sqrt(distanceSquared)) <= BATTLE_RADIUS) {
            // mount player as in range
            world.getPlayer().registerEntity(this);
        } else {
            world.getPlayer().unregisterEntity(this);
        }
        
        getMovement().move(this, world);
        setInteractable(world.getPlayer());
    }

    private void setInteractable(Player player) {
        Position relativePos = Position.calculatePositionBetween(player.getPosition(), this.getPosition());
        if (getAlly()) {
            interactable = false;
        } else if ((relativePos.getX() + relativePos.getY()) <= 2) {
            interactable = true;
        } else {
            interactable = false;
        }
    }

    // TODO this needs to be updated
    public void update(Movement movement) {
        // take in duration left,
        //after duration is 0 revert back to normal pattern;
    }


    /**
     *  
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     */
    public void interact(World world) throws InvalidActionException {
        if (interactable) {
            if (world.numItemInInventory("treasure") >= GOLD_TO_BRIBE) {
                for (int i = 0; i < GOLD_TO_BRIBE; i++) {
                    world.useByType("treasure");
                }
                setAlly(true);
            } else {
                throw new InvalidActionException("Not enough gold to bribe Mercenary!");
            }
        } else {
            throw new InvalidActionException("Must be within 2 cardinal tiles to bribe Mercenary!");
        }
    }


    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), interactable);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject mercJSON = super.saveGameJson();
        JSONObject movement = new JSONObject();

        movement.put("default-movement", defaultMovementStrategy.getMovementType());
        movement.put("movement-strategy", movementStrategy.getMovementType());
        
        mercJSON.put("movement", movement);

        return mercJSON;
    }
}
