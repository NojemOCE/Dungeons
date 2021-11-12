package dungeonmania.staticEntityTest.logicTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import dungeonmania.logic.OrLogic;
import dungeonmania.staticEntity.LightBulb;
import dungeonmania.util.Position;

public class LightBulbTest {
    @Test
    public void constructionTest(){
        // Constructing a boulder
        Position lightPos = new Position(1, 1);
        LightBulb lightbulb = new LightBulb(lightPos.getX(), lightPos.getY(), "light_bulb1", new OrLogic());
        assertNotNull(lightbulb);
        assert(lightPos.equals(lightbulb.getPosition()));
    }
}
