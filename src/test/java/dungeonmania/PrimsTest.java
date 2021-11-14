package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;


import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class PrimsTest {

    @Test
    public void testCollectItem() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse d = controller.generateDungeon(1, 1, 6, 6, "hard");
        assertNotNull(d);
    }
}
