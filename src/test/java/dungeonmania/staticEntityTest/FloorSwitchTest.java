package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


public class FloorSwitchTest {
    @Test
    public void constructionTest(){
        Position floorSwitchPos = new Position(1,1);
        FloorSwitch floorSwitch = new FloorSwitch(floorSwitchPos);
        assertNotNull(floorSwitch);
        assert(floorSwitchPos.equals(floorSwitch.getPosition()));

    }

    @Test
    public void coverTest(){
        Position floorSwitchPos = new Position(1,1);
        FloorSwitch floorSwitch = new FloorSwitch(floorSwitchPos);
        
        assertEquals(floorSwitch.isTriggered(), false);

        floorSwitch.trigger();
        assertEquals(floorSwitch.isTriggered(), true);

    }

}
