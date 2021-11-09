package dungeonmania.movingEntity.MovementStrategies;

import java.util.Objects;

import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.movingEntity.*;

import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CircleMovement implements MovementStrategy {
    private Direction currentDirection;
    private Direction nextDirection;
    private int remMovesCurr;
    private int remMovesNext;
    private boolean avoidPlayer;
    static final int START_MOVES = 1;
    static final int SET_MOVES = 2;
    
    

    public CircleMovement() {
        this.currentDirection = Direction.UP;
        this.nextDirection = Direction.RIGHT;
        this.remMovesCurr = START_MOVES;
        this.remMovesNext = START_MOVES;
        this.avoidPlayer = false;
    }

    public CircleMovement(Direction currentDirection, Direction nextDirection, int remMovesCurr, int remMovesNext, boolean avoidPlayer) {
        this.currentDirection = currentDirection;
        this.nextDirection = nextDirection;
        this.remMovesCurr = remMovesCurr;
        this.remMovesNext = remMovesNext;
        this.avoidPlayer = avoidPlayer;
    }

    @Override
    public void move(MovingEntity me, World world) {
        // First get the planned next position
        Position plannedNextPosition = plannedNextPosition(me);

        // Determine what the actual new position should be
        Position newPosition = validMove(plannedNextPosition, world, me);

        moveTo(newPosition, me);
        
    }

    /**
     * Takes the intended movement position, the world, and the moving entity, and returns the actual position the entity should move to
     * @param position intended movement position of the entity
     * @param world current game world
     * @param me moving entity
     * @return actual position entity should move to 
     */
    private Position validMove(Position position, World world, MovingEntity me) {
        // Check if the planned position is the current location of a static entity or a character
        StaticEntity plannedPositionStaticEntity = world.getStaticEntity(position);
        MovingEntity plannedPositionCharacter = world.getCharacter(position);
        Position playerPos = world.getPlayerPosition();

        // if in battle, don't move (can't run away)
        if (!Objects.isNull(world.getBattle())) {
            // check if this objects position is same as players (for players if there is a battle)
            // they cannot move anyways
            if (me.getPosition().equals(world.getPlayer().getPosition())) {
                // cannot move into battle, wait outside
                return me.getPosition();
            }
            
        }


        // Now interact with the StaticEntity in the intended position 
        // and check if if the entity is moving into the position of another enemy Character
        Boolean movingIntoEnemy = (plannedPositionCharacter != null) && !(plannedPositionCharacter instanceof Player);
        
        if (!(plannedPositionStaticEntity == null))  {
           position = plannedPositionStaticEntity.interact(world, me); 
        }

        // If spider is avoiding player (if the player is invincible), and the spider is about to move into the player, set the move positionn as current position
        // This will later ensure that direction is reversed
        if (avoidPlayer && playerPos.equals(position)) {
            position  = me.getPosition();
        }
        

        // If entity is moving into a boulder or an enemy, reverse direction
        // and return the validMove position as the entity's current position
        if (position.equals(me.getPosition()) || movingIntoEnemy) {
            reverseDirection();
            return me.getPosition();
        }
        else {
            return position;
        }
    }


    /**
     * Returns the position that the entity would next move to in its circle patternn
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
        Direction d;
        // If a starting move we want to reverse next direction
        if (remMovesCurr == 0 && remMovesNext  == 1) {
            d = oppositeDirection(nextDirection);
            Direction dNext = oppositeDirection(currentDirection);
            
            currentDirection = d;
            nextDirection = dNext;

            remMovesCurr = remMovesNext;
            remMovesNext = SET_MOVES;
        }
        else {
            currentDirection  = oppositeDirection(currentDirection);

            if (remMovesCurr == 0) {
                remMovesCurr = SET_MOVES;
            }
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
        if (!(p.equals(me.getPosition()))) {
            remMovesCurr -= 1;
        }
        
        me.setPosition(p);
        
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

    @Override
    public String getMovementType() {
        return "circleMovement";
    }

    /*public String getCurrentDirection() {
        return getDirectionString(currentDirection);
    }
    public String getNextDirection() {
        return getDirectionString(nextDirection);
    }*/


    /**
     * Gets a given direction as a string
     * @param d direction
     * @return string of direction, eg. Direction.UP becmes "UP"
     */
    private String getDirectionString(Direction d) {
        switch(d)  {
            case UP:
                return "UP";
            case DOWN:
                return "DOWN";
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
            default:
                return "NONE";
        }
    }

    /**
     * Sets the movement strategy to avoid the player
     * @param avoidPlayer boolean to determine if the player should be avoided
     */
    public void setAvoidPlayer(boolean avoidPlayer) {
        this.avoidPlayer = avoidPlayer;
    }

    @Override
    public JSONObject getMovementJson() {
        JSONObject obj = new JSONObject();
        obj.put("movement", getMovementType());
        obj.put("current-direction", getDirectionString(currentDirection));
        obj.put("next-direction", getDirectionString(nextDirection));
        obj.put("remMovesCurr", remMovesCurr);
        obj.put("remMovesNext", remMovesNext);
        obj.put("avoidPlayer", avoidPlayer);
        
        return obj;
    }
    
}
