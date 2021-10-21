package dungeonmania.character;

import dungeonmania.World;
import dungeonmania.util.*;

public interface Movement {

    public void move(World world);

    public Position validMove(Position position, World world);

}
