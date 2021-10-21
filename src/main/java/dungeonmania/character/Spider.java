package dungeonmania.character;

import dungeonmania.util.*;


public class Spider extends Character {
    private Direction currentDirection;
    private Direction nextDirection;
    private int remMovesCurr;
    private int remMovesNext;
    static final int START_MOVES_IN_DIRECTION = 1;
    static final int SET_MOVES_IN_DIRECTION = 2;


    public Spider(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        currentDirection = Direction.UP;
        nextDirection = Direction.RIGHT;
        remMovesCurr = START_MOVES_IN_DIRECTION;
        remMovesNext = START_MOVES_IN_DIRECTION;
    }

    @Override
    public void move(Direction direction) {// will actually take world, not direction
        
        // get planned next position
        Position plannedNextPosition = plannedNextPosition();

        //CHECK IF NEXT POSITION IS A BOULDER OR ANOTHER SPIDER
        /**
         * if next position == boulder or spider
         *    currDirection  = opposite(currentDirection);
         * 
         *    if remMoveCurr ==0 {
         *      remMovesCurr = SET_MOVES_IN_DIRECTION
         *    }
         * else {
         *      if (remMovesCurr == 0) {
         *         updateDirection()}
         *      setPosition(plannedNextPosition)
         *      remMovesCurr -=1
         * MOVE TO NEW POSITION}
         * 
         */


        /*if (remMovesCurr == 0) {
            // Check adjacent position
            updateDirection();
        }

        
        Position newPosition = getPosition().translateBy(currentDirection);

        // Basic movement need to account for boulder before this
        this.setPosition(newPosition);
        remMovesCurr -=1;

        return;*/
    }

    // Get the spiders planned next position
    private Position plannedNextPosition() {
        if (remMovesCurr != 0) {
            return getPosition().translateBy(currentDirection);
        }
        else {
            return getPosition().translateBy(nextDirection);
        }
    }

    private void updateDirection() {
        remMovesCurr = remMovesNext;
        remMovesNext = SET_MOVES_IN_DIRECTION;

        Direction newNextDirection = oppositeDirection(currentDirection);
        currentDirection = nextDirection;
        nextDirection = newNextDirection;
    }

    // I think this method would be better placed as a public method in the DIRECTION CLASS, but storing it here for now
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
