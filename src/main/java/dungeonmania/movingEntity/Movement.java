package dungeonmania.movingEntity;

import dungeonmania.World;
import dungeonmania.util.*;
import dungeonmania.*;

public interface Movement {

    // happens per tick
    public void move(MovingEntity me, World world);


}
