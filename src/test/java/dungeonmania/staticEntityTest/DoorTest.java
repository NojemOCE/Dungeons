package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class DoorTest {
    @Test
    public void constructionTest(){

    Position doorPos = new Position(1, 1);
        // Using a null key for now
        Door door = new Door(doorPos, null);
        assertNotNull(door);
        assert(doorPos.equals(door.getPosition()));

    }
}
