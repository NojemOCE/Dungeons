package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class DoorTest {
    @Test
    public void constructionTest(){
        Position doorPos = new Position(1, 1);
        int keyColour = 1;
        Door door = new Door(doorPos.getX(), doorPos.getX(), "door1", keyColour);
        assertNotNull(door);
        assert(doorPos.equals(door.getPosition()));
    }
}
