package dungeonmania.movingEntity.MovementStrategies;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import dungeonmania.World;
import dungeonmania.movingEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class RandomMovement implements Movement {

    @Override
    public void move(MovingEntity me, World world) {
       randomMovement(me, world);
    }

    /**
     * Simulates random movement behavior for an entity and moves them 1 square
     * @param me entity to move
     * @param world current game world
     */
    private void randomMovement(MovingEntity me, World world) {
        Random randomMovement = new Random();

        int upperbound = 4;
        int random = randomMovement.nextInt(upperbound);
        switch(random) {
            case 0:
                me.setPosition(me.validMove(me.getPosition().translateBy(Direction.UP), world)); 
                break;
            case 1:
                me.setPosition(me.validMove(me.getPosition().translateBy(Direction.DOWN), world));
                break;
            case 2:
                me.setPosition(me.validMove(me.getPosition().translateBy(Direction.LEFT), world));
                break;
            case 3:
                me.setPosition(me.validMove(me.getPosition().translateBy(Direction.RIGHT), world));
                break;
            default:
                // no change in position
                me.setPosition(me.getPosition());
        }        
    }

    @Override
    public String getMovementType() {
        return "randomMovement";
    }
}
