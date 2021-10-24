package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.goal.ObserverTreasureGoal;
import dungeonmania.goal.SubjectBoulderSwitchGoal;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class Boulder extends StaticEntity {
    
    public Boulder(int x, int y, String id) {
        super(new Position(x, y, 1), id, "boulder");

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
            // check that nothing is in this spot (in layer one)
            // need a list of ALL entities?
            // map to check what entities are on that spot
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

    public void move(Direction d) {

    }


}
