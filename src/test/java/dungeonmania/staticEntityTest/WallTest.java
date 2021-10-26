package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class WallTest {

    @Test
    public void constructionTest(){

        Position wallPos = new Position(1,1);
        Wall wall = new Wall(wallPos);
        assertNotNull(wall);
        assert(wallPos.equals(wall.getPosition()));

    }
}
