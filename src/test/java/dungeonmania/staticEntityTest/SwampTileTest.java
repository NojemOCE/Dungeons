package dungeonmania.staticEntityTest;

import org.junit.jupiter.api.Test;

import dungeonmania.staticEntity.SwampTile;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    public void constructionTest(){

        Position swampPos = new Position(1,1);
        SwampTile swamp = new SwampTile(swampPos.getX(), swampPos.getY(), "swamp1");
        assertNotNull(swamp);
        assert(swampPos.equals(portal.getPosition()));
    
    }

}
