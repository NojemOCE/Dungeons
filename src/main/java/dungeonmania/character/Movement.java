package dungeonmania.character;

import dungeonmania.util.*;

public interface Movement {

    // happens per tick
    public void move(Position position);

    public void validMove(Position position);
}
