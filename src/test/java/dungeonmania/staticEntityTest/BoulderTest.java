package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;



public class BoulderTest {
    @Test
    public void constructionTest(){
        // Constructing a boulder
        Position boulderPos = new Position(1,1);
        Boulder boulder = new Boulder(boulderPos);
        assertNotNull(boulder);
        assert(boulderPos.equals(boulder.getPosition()));
    }

    @Test
    public void boulderMove(){
        // Constructing a boulder
        Position boulderPos = new Position(1,1);
        Boulder boulder = new Boulder(boulderPos);
        assert(boulderPos.equals(boulder.getPosition()));

        // This depends on the layout of the board. 
        // Currently assuming it is 1..n from left to right and 1..n from top to bottom
        boulder.move(Direction.DOWN);
        Position expectedPosition = new Position(1, 2);
        
        assert(expectedPosition.equals(boulder.getPosition()));
        
    }

}
