package dungeonmania.character;

import dungeonmania.World;
import dungeonmania.util.*;
import dungeonmania.*;

public interface Movement {

    // happens per tick
    public void move(World world);


    public Position validMove(Position position, World world);
    // this will be called inside move prior to moving
}
