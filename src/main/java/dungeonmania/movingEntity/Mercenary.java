package dungeonmania.movingEntity;

import dungeonmania.util.*;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.MovementStrategies.FollowPlayer;
import dungeonmania.movingEntity.MovementStrategies.RandomMovement;
import dungeonmania.movingEntity.MovementStrategies.RunAway;
import dungeonmania.movingEntity.States.State;
import dungeonmania.response.models.EntityResponse;


public class Mercenary extends MovingEntity implements PlayerPassiveObserver {

    static final int MERC_ATTACK = 3;
    static final int MERC_HEALTH = 20;
    private static final int GOLD_TO_BRIBE = 1;
    private static final double BATTLE_RADIUS = 8;
    private boolean interactable = false;

    /**
     * Constructor for Mercenary taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id unique entity id of the mercenary
     */
    public Mercenary(int x, int y, String id) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "mercenary", new HealthPoint(MERC_HEALTH), MERC_ATTACK);
        setMovement(new FollowPlayer());
        setDefaultMovementStrategy(new FollowPlayer());
        setAlly(false);
    }

    /**
     * Constructor for mercenary taking x, y coordinates, id, Healthpoint, default movement strategy, current movement strategy, whether the mercenary is an ally, and the state of the mercenary
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id iunique entity id of the mercenary
     * @param hp healthpoint object of the mercenary
     * @param defaultMovement default movement strategy of the mercenary
     * @param currentMovement current movement strategy of the mercenary
     * @param isAlly whether the mercenary is an ally (true or false)
     * @param state state of the mercenary's movement (normal or swamp)
     */
    public Mercenary(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement, MovementStrategy currentMovement, Boolean isAlly, State state) {
        super(new Position(x, y, Position.MOVING_LAYER), id, "mercenary", hp, MERC_ATTACK);
        setMovement(currentMovement);
        setDefaultMovementStrategy(defaultMovement);
        setAlly(isAlly);
        setState(state);
    }

    @Override
    public void move(World world) {
        getState().move(this, world);
    }

    @Override
    public void moveEntity(World world) {

        Position distance = Position.calculatePositionBetween(world.getPlayer().getPosition(), this.getPosition());
        double x = (double)distance.getX();
        double y = (double)distance.getY();
        double distanceSquared = ((x*x) + (y*y));
        
        if ((Math.sqrt(distanceSquared)) <= BATTLE_RADIUS) {
            // mount player as in range
            world.getPlayer().addInRange(this);
        } else {
            world.getPlayer().removeInRange(this);
        }
        
        getMovement().move(this, world);
        setInteractable(world.getPlayer());
        
        checkSwampTile(world);
    }

    /**
     * Sets the interactability of mercenary
     * @param player player
     */
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

    // TODO !!!!!!!! wouldn't this method be better off in world? why should the merc check if the player has enough treasure or is within 2 tiles. I think the player should do this
    /**
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     * @param world current world that the mercenary is in
     * @throws InvalidActionException if the player does not have enough gold, or if the player is not within 2 cardinal tiles of the mercenary
     */
    public void interact(World world) throws InvalidActionException {
        if (interactable) {
            if (world.numItemInInventory("treasure") >= GOLD_TO_BRIBE) {
                for (int i = 0; i < GOLD_TO_BRIBE; i++) {
                    world.useByType("treasure");
                }
                setAlly(true);
                world.setBattle(null);
                setSpeed(0);
            } else {
                throw new InvalidActionException("Not enough gold to bribe Mercenary!");
            }
        } else {
            throw new InvalidActionException("Must be within 2 cardinal tiles to bribe Mercenary!");
        }
    }

    @Override
    public void updateMovement(String passive) {
        if (passive.equals("invincibility_potion") && !getAlly()) {
            setMovement(new RunAway());
        } else if (passive.equals("invisibility_potion") && !getAlly()) {
            setMovement(new RandomMovement());
        } else {
            setMovement(getDefaultMovementStrategy());
        }
        
    }

    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), getType(), getPosition(), interactable);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject mercJSON = super.saveGameJson();

        mercJSON.put("default-strategy", defaultMovementStrategy.getMovementJson());
        mercJSON.put("movement-strategy", movementStrategy.getMovementJson());
        mercJSON.put("state", getState().getStateJson());
        mercJSON.put("ally", getAlly());

        return mercJSON;
    }

}
