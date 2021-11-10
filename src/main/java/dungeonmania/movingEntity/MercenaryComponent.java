package dungeonmania.movingEntity;

import dungeonmania.collectable.Sceptre;
import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.MovementStrategies.*;
import dungeonmania.movingEntity.States.State;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class MercenaryComponent extends MovingEntity implements PlayerPassiveObserver {

    private static final double BATTLE_RADIUS = 8;
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
        } else if ((relativePos.getX() + relativePos.getY()) <= 2) {
            interactable = true;
        } else {
            interactable = false;
        }
    }

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
        JSONObject movement = new JSONObject();

        movement.put("default-strategy", defaultMovementStrategy.getMovementType());
        movement.put("movement-strategy", movementStrategy.getMovementType());
        
        mercJSON.put("movement", movement);
        mercJSON.put("ally", getAlly());

        return mercJSON;
    }

}
