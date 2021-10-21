package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ExitTest {
    @Test
    public void constructionTest(){

        Position exitPos = new Position(1,1);
        Exit exit = new Exit(exitPos);
        assertNotNull(exit);
        assert(exitPos.equals(exit.getPosition()));

    }
}
