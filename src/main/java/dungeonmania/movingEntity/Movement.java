package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.util.*;
import dungeonmania.*;

public interface Movement {

    /**
     * Takes a moving entity and the current game world, and moves them to their next position
     * @param me entity to move
     * @param world current game world
     */
    public void move(MovingEntity me, World world);

    public String getMovementType();
}
