package dungeonmania.staticEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class Boulder extends StaticEntity {
    
    /**
     * Constructor for boulder
     * @param x x coordinate of the boulder
     * @param y y coordinate of the boulder
     * @param id id of the boulder
     */
    public Boulder(int x, int y, String id) {
        super(new Position(x, y, 1), id, "boulder");
    }

    /**
     * Acts like a wall in most cases. The only difference is that it can 
     * be pushed by the character into cardinally adjacent squares. The 
     * character is only strong enough to push one boulder at a time.
     */
    @Override
    public Position interact(World world, Entity entity) {

        if (entity instanceof Player) {
            Position toMoveBoulderTo = move(entity);
            
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
            if (!portals.isEmpty()) {
                Portal portal = portals.get(0);
                toMoveBoulderTo = portal.interact(world, this);
            }

            Position playertoMoveTo = this.getPosition();

            // move boulder then return appropriate position for character to move to
            this.setPosition(toMoveBoulderTo);
            return playertoMoveTo;
        }
        
        
        return entity.getPosition();
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
        // if anything is on the same layer or higher, can't move
        if (entitiesAtNewPos.stream().anyMatch(x -> x.getPosition().getLayer() >= this.getPosition().getLayer())) {
            return false;
        }
        return true;
    }
    
    
    /**
     * Gets the position that the boulder should move to
     * @param character the character trying to move the boulder
     * @return The position that the boulder should move to
     */
    private Position move(Entity character) {
        // get relative position 
        int charX = character.getPosition().getX();
        int charY = character.getPosition().getY();
        int boulderX = this.getPosition().getX();
        int boulderY = this.getPosition().getY();
    
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


}
