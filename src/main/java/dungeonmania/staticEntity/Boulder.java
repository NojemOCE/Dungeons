package dungeonmania.staticEntity;

import java.util.List;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class Boulder extends StaticEntity {
    
    public Boulder(Position position) {
        super(position);
    }

    /**
     * Acts like a wall in most cases. The only difference is that it can 
     * be pushed by the character into cardinally adjacent squares. The 
     * character is only strong enough to push one boulder at a time.
     */
    @Override
    public Position interact(World world, MovingEntity character) {

        if (character instanceof Player) {
            Position toMoveBoulderTo = move(character);
            List<StaticEntity> entitiesAtNewPos = world.getStaticEntitiesAtPosition(toMoveBoulderTo);
            // if anything is on the same layer, can't move
            if (entitiesAtNewPos.stream().anyMatch(x -> x.getPosition().getLayer() == this.getPosition().getLayer())) {
                return character.getPosition();
            }

            // otherwise move
            List<StaticEntity> entitiesAtThisPos = world.getStaticEntitiesAtPosition(this.getPosition());
            // untrigger any switch in the previous spot
            entitiesAtThisPos.stream()
                            .filter(x -> x instanceof FloorSwitch)
                            .map(FloorSwitch.class::cast)
                            .forEach(x -> x.untrigger());

            // trigger any switch in the new spot
            entitiesAtNewPos.stream()
                            .filter(x -> x instanceof FloorSwitch)
                            .map(FloorSwitch.class::cast)
                            .forEach(x -> x.trigger());
            // move boulder then return appropriate position for character to move to
            this.setPosition(toMoveBoulderTo);
            return this.getPosition();
        }
        
        
        return character.getPosition();
    }
    
    
    /**
     * Gets the position that the boulder should move to
     * @param character the character trying to move the boulder
     * @return The position that the boulder should move to
     */
    private Position move(MovingEntity character) {
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

    // Not sure this is required
    public void move(Direction d) {

    }

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "boulder", getPosition(), false);
    }
}
