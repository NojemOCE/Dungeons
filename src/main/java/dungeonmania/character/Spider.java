package dungeonmania.character;

import dungeonmania.World;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.*;


public class Spider extends Character {
    private Direction currentDirection;
    private Direction nextDirection;
    private int remMovesCurr;
    private int remMovesNext;
    static final int START_MOVES = 1;
    static final int SET_MOVES = 2;


    public Spider(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        currentDirection = Direction.UP;
        nextDirection = Direction.RIGHT;
        remMovesCurr = START_MOVES;
        remMovesNext = START_MOVES;
    }




    // To be uncommented
    //@Override
    /**
     * Simulates 1 ticks worth of movement for the spider. Note: if the spiders
     * movement is obstructed, then its direction will reverse on this tick,
     * and movement will recommence the following tick
     * @param world World in which the spider is a character of
     */
    public void move(World world) {
        
        // First get the planned position of the spider
        Position plannedNextPosition = plannedNextPosition();

        // Next check if the planned position is the current location of a static entity or a character
        // Null for now but needs to be updated when world method is implemented
        StaticEntity plannedPositionStaticEntity = null; // getStaticEntity(Position plannedNextPosition)
        Character plannedPositionCharacter = null; // getCharacter(Position plannedNextPosition)

        // Now check if the StaticEntity Spider is moving into is a boulder, 
        // or if the Spider is moving into the position of another enemy Character
        Boolean movingIntoBoulder = (plannedPositionStaticEntity != null) && (plannedPositionStaticEntity instanceof Boulder);
        Boolean movingIntoEnemy = (plannedPositionCharacter != null) && !(plannedPositionCharacter instanceof Player);

        // If Spider is moving into a boulder or an enemy, reverse direction
        if (movingIntoBoulder || movingIntoEnemy) {
            reverseDirection();
        }
        // Otherwise the Spider should move to its planned position
        else {
            moveTo(plannedNextPosition);
        }
    }

    /**
     * Returns the position that the spider would next move to
     * @return Spider's next planned position
     */
    private Position plannedNextPosition() {
        if (remMovesCurr != 0) {
            return getPosition().translateBy(currentDirection);
        }
        else {
            return getPosition().translateBy(nextDirection);
        }
    }

    /**
     * Reverses the spiders current direction (called if the spider wants to
     * move into the position of a boulder, or another enemy). If the spider was
     * at its last move in the current direction, it resets the the number of 
     * moves that spider has in its new current direction
     */
    private void reverseDirection(){
        currentDirection  = oppositeDirection(currentDirection);
            if (remMovesCurr == 0) {
                remMovesCurr = SET_MOVES;
            }
    }

    /**
     * Takes a Position p and moves the spider to this position
     * @param p Position to move Spider to
     */
    private void moveTo(Position p) {
        if (remMovesCurr ==0) {
            updateDirection();
        }
        setPosition(p);
        remMovesCurr -=1;
    }

    /**
     * Updates the spiders current direction to it's next direction, 
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

    @Override
    public boolean validMove(Position position, World world) {
        // TODO Auto-generated method stub
        
    }
}
