package dungeonmania.staticEntityTest.logicTest;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class logicTest {
    @Test
    public void basicTest() {
        // Create a new world
        World world = new World("logic-test", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "logic-test" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        DungeonResponse d = world.tick(null, Direction.RIGHT);

    }
}
