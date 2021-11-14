package dungeonmania.staticEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.States.*;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class Boulder extends StaticEntity {
    private State state;
    
    /**
     * Constructor for boulder
     * @param x x coordinate of the boulder
     * @param y y coordinate of the boulder
     * @param id id of the boulder
     */
    public Boulder(int x, int y, String id) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "boulder");
        this.state = new NormalState();
    }

    /**
     * Constructor for boulder from save state
     * @param x x coordinate of the boulder
     * @param y y coordinate of the boulder
     * @param id id of the boulder
     * @param state state of the boulder (swamp state or normal)
     */
    public Boulder(int x, int y, String id, State state) {
        this(x, y, id);
        this.state = state;
    }

    /**
     * Acts like a wall in most cases. The only difference is that it can 
     * be pushed by the character into cardinally adjacent squares. The 
     * character is only strong enough to push one boulder at a time.
     */
    @Override
    public Position interact(World world, Entity entity) {

        if (entity instanceof Player) {
            Position toMoveBoulderTo = state.move(this, (Player) entity, world);
            // move(entity);
            
            if (!validMove(world, toMoveBoulderTo)) {
                return entity.getPosition();
            }
            
            // otherwise move
            List<StaticEntity> entitiesAtThisPos = world.getStaticEntitiesAtPosition(this.getPosition());
            List<StaticEntity> entitiesAtNewPos = world.getStaticEntitiesAtPosition(toMoveBoulderTo);

            // SWITCHES:
            // untrigger any switch in the previous spot
            entitiesAtThisPos.stream()
                             .filter(x -> x instanceof FloorSwitch)
                             .map(FloorSwitch.class::cast)
                             .forEach(x -> x.untrigger());

            // trigger any switch in the new spot
            entitiesAtNewPos.stream()
                            .filter(x -> x instanceof FloorSwitch)
                            .map(FloorSwitch.class::cast)
                            .forEach(x -> x.trigger(world));


            // PORTALS:
            List<Portal> portals = entitiesAtNewPos.stream()
                                                   .filter(x -> x instanceof Portal)
                                                   .map(Portal.class::cast)
                                                   .collect(Collectors.toList());

            Position entityToMoveTo = new Position(getX(), getY(), entity.getLayer());

            // if there is a portal check if we can move
            // if we cant then entity also can't move
            if (!(portals.isEmpty())) {
                Portal portal = portals.get(0);
                toMoveBoulderTo = portal.interact(world, this);
                if (toMoveBoulderTo.equals(getPosition())) {
                    return entity.getPosition();
                }
            }

            // if we reach here then we move both the boulder and player
            // move boulder then return appropriate position for character to move to
            updatePosition(toMoveBoulderTo, world, entitiesAtNewPos);
            return entityToMoveTo;
        }
        
        
        return entity.getPosition();
    }
    // TODO: do we check for switch AFTER portal?!?!?

    /**
     * Update the position and state of the boulder
     * @param toMoveTo position to move boulder to
     * @param world instance of current world
     * @param entities entities at position to move to
     */
    private void updatePosition(Position toMoveTo, World world, List<StaticEntity> entities) {
        this.setPosition(new Position(toMoveTo.getX(), toMoveTo.getY(), getLayer()));
        // update state (if we move to a swamp tile)
        for (StaticEntity e :  entities) {
            if (e instanceof SwampTile) {
                int ticks = ((SwampTile) e).getMovementFactor();
                setState(new SwampState(ticks));
            }
        }
    }

    /**
     * Checks if a move to the given position is valid
     * - due to design, if there is anything on the same 
     *   layer or higher, it cannot be moved
     * @param world current world
     * @param position position to move to
     * @return whether the boulder can move to the given position
     */
    public boolean validMove(World world, Position position) {
        List<StaticEntity> entitiesAtNewPos = world.getStaticEntitiesAtPosition(position);
        // if anything is on the same layer or higher, can't move, unless it is a portal
        List<Portal> portals = entitiesAtNewPos.stream()
                                               .filter(x -> x instanceof Portal)
                                               .map(Portal.class::cast)
                                               .collect(Collectors.toList());
        if (!(portals.isEmpty())) {
            return true;
        }
        if (entitiesAtNewPos.stream().anyMatch(x -> x.getLayer() >= this.getLayer())) {
            return false;
        }
        return true;
    }
    
    
    /**
     * Gets the position that the boulder should move to
     * @param character the character trying to move the boulder
     * @return The position that the boulder should move to
     */
    public Position move(Entity character) {
        // get relative position 
        int charX = character.getX();
        int charY = character.getY();
        int boulderX = this.getX();
        int boulderY = this.getY();
    
        if (charY == boulderY) {
            // if left of
            if (charX < boulderX) {
                return this.getPosition().translateBy(Direction.RIGHT);
            } else {
                // is right of
                return this.getPosition().translateBy(Direction.LEFT);
            }
        } else {
            // if above
            if (charY < boulderY) {
                return this.getPosition().translateBy(Direction.DOWN);
            } else {
                // is below
                return this.getPosition().translateBy(Direction.UP);
            }
        }
    }

    /**
     * Getter for movement state
     * @return the movement state
     */
    public State getState() {
        return state;
    }

    /**
     * Setter for movement state
     * @param state current movement state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    @Override
	public JSONObject saveGameJson() {
		JSONObject obj = super.saveGameJson();
        obj.put("state", state.getStateJson());
		return obj;
	}
}
