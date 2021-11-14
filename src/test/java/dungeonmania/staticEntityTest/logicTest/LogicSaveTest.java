package dungeonmania.staticEntityTest.logicTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public class LogicSaveTest {
    @Test
    public void testLogicSave() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("test-logic-save", "peaceful");

        controller.tick(null, Direction.DOWN);

        controller.saveGame("logic-save");

        List<String> games = controller.allGames();

        assert(games.size() > 0);

        assertDoesNotThrow(() -> controller.loadGame("logic-save"));
    }
}
