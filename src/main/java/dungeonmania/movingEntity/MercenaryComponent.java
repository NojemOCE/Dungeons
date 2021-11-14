package dungeonmania.movingEntity;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.*;
import dungeonmania.movingEntity.States.State;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class MercenaryComponent extends MovingEntity implements PlayerPassiveObserver, MindControl {

    private static final double BATTLE_RADIUS = 8;
    private static final int BRIBE_DISTANCE = 2;
    private boolean interactable = false;

    /**
     * Constructor for Mercenary taking an x coordinate, and y coordinate and an id
     * @param x x coordinate of the mercenary
     * @param y y coordinate of the mercenary
     * @param id unique entity id of the mercenary
     */
    public MercenaryComponent(int x, int y, String id, String type, HealthPoint health, double attack) {
        super(new Position(x, y, Position.MOVING_LAYER), id, type, health, attack);
        setMovement(new FollowPlayer());
        setDefaultMovementStrategy(new FollowPlayer());
        setAlly(false);
    }

    /**
     * Constructor for mercenary component taking x and y coords, and unique ID
     * @param x x coordinate
     * @param y y coordinate
     * @param id unique ID of mercenary component
     * @param health health point of mercenary component
     * @param attack double for attack
     * @param defaultMovement default movement
     * @param currentMovement current movement
     * @param isAlly boolean for whether the mercenary component is an ally
     * @param state movement state
     */
    public MercenaryComponent(int x, int y, String id, String type, HealthPoint health, double attack, MovementStrategy defaultMovement, MovementStrategy currentMovement, Boolean isAlly, State state) {
        super(new Position(x, y, Position.MOVING_LAYER), id, type, health, attack);
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
    protected void setInteractable(Player player) {
        Position relativePos = Position.calculatePositionBetween(player.getPosition(), this.getPosition());
        if (getAlly()) {
            interactable = false;
        } else if ((Math.abs(relativePos.getX()) + (Math.abs(relativePos.getY()))) <= BRIBE_DISTANCE) {
            interactable = true;
        } else {
            interactable = false;
        }
    }

    /**
     * Getter for interactable
     * @return interactable
     */
    protected boolean getInteractable() {
        return this.interactable;
    }

    /**
     *
     * The character can bribe a mercenary if they are within 2 cardinal tiles
     * to the mercenary. Player requires minimum amount of gold to bribe.
     * @param world
     */
    public abstract void interact(World world);

    @Override
    public void updateDuration(int effectDuration) {
        setAlly(effectDuration != 0);
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

    /**
     * Interactable (flashing) at all times until made ally
     */
    @Override
    public EntityResponse getEntityResponse() {
        if (getAlly()) {
            return new EntityResponse(getId(), getType(), getPosition(), false);
        }
        return new EntityResponse(getId(), getType(), getPosition(), true);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject mercJSON = super.saveGameJson();

        mercJSON.put("default-strategy", defaultMovementStrategy.getMovementJson());
        mercJSON.put("movement-strategy", movementStrategy.getMovementJson());

        mercJSON.put("ally", getAlly());
        mercJSON.put("state", getState().getStateJson());

        return mercJSON;
    }

}
