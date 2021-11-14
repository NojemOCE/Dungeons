package dungeonmania.staticEntityTest.logicTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import dungeonmania.staticEntity.Wire;
import dungeonmania.util.Position;

public class WireTest {
    @Test
    public void constructionTest(){
        // Constructing a boulder
        Position wirePos = new Position(1, 1);
        Wire wire = new Wire(wirePos.getX(), wirePos.getY(), "wire1");
        assertNotNull(wire);
        assert(wirePos.equals(wire.getPosition()));
    }
}
