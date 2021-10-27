package dungeonmania.movingEntity.MovementStrategies;

import dungeonmania.World;
import dungeonmania.movingEntity.*;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CircleMovement implements Movement {
    private Direction currentDirection;
    private Direction nextDirection;
    private int remMovesCurr;
    private int remMovesNext;
    static final int START_MOVES = 1;
    static final int SET_MOVES = 2;
    

    @Override
    public void move(MovingEntity me, World world) {
        // First get the planned next position
        Position plannedNextPosition = plannedNextPosition(me);

        // Determine what the actual new position should be
        Position newPosition = validMove(plannedNextPosition, world, me);

        moveTo(newPosition, me);
        
    }

    private Position validMove(Position position, World world, MovingEntity me) {
        // Check if the planned position is the current location of a static entity or a character
        StaticEntity plannedPositionStaticEntity = world.getStaticEntity(position);
        MovingEntity plannedPositionCharacter = world.getCharacter(position);

        // Now check if the StaticEntity the entity is moving into is a boulder, 
        // or if the entity is moving into the position of another enemy Character
        Boolean movingIntoBoulder = (plannedPositionStaticEntity != null) && (plannedPositionStaticEntity instanceof Boulder);
        Boolean movingIntoEnemy = (plannedPositionCharacter != null) && !(plannedPositionCharacter instanceof Player);

        // If entity is moving into a boulder or an enemy, reverse direction
        // and return the validMove position as the entity's current position
        if (movingIntoBoulder || movingIntoEnemy) {
            reverseDirection();
            return me.getPosition();
        }
        else {
            return position;
        }
    }


    /**
     * Returns the position that the entity would next move to
     * @return entity's next planned position
     */
    private Position plannedNextPosition(MovingEntity me) {
        if (remMovesCurr != 0) {
            return me.getPosition().translateBy(currentDirection);
        }
        else {
            return me.getPosition().translateBy(nextDirection);
        }
    }

    /**
     * Reverses the entity's current direction (called if the entity wants to
     * move into the position of a boulder, or another enemy). If the entity was
     * at its last move in the current direction, it resets the the number of 
     * moves that entity has in its new current direction
     */
    private void reverseDirection(){
        currentDirection  = oppositeDirection(currentDirection);
            if (remMovesCurr == 0) {
                remMovesCurr = SET_MOVES;
            }
    }

    /**
     * Takes a Position p and moves the entity to this position
     * @param p Position to move entity to
     */
    private void moveTo(Position p, MovingEntity me) {
        if (remMovesCurr == 0) {
            updateDirection();
        }
        me.setPosition(p);
        remMovesCurr -= 1;
    }

    /**
     * Updates the current direction to the next direction, 
     * changes the remaining number of moves in the current direction to the 
     * set number of moves in the next direction, and resets the number of moves
     * in the next direction to SET_MOVES = 2
     */
    private void updateDirection() {
        remMovesCurr = remMovesNext;
        remMovesNext = SET_MOVES;

        Direction newNextDirection = oppositeDirection(currentDirection);
        currentDirection = nextDirection;
        nextDirection = newNextDirection;
    }


    // I think this method would be better placed as a public method in the DIRECTION CLASS, but storing it here for now
    /**
     * Takes a Direction d, and returns the direction that is opposite to it
     * @param d Direction to find opposite of
     * @return opposite Direction to d
     */
    private Direction oppositeDirection(Direction d) {
        switch(d)  {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            default:
                return Direction.NONE;
        }
    }

    
}
