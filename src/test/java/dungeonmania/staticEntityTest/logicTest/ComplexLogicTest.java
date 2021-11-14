package dungeonmania.staticEntityTest.logicTest;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class ComplexLogicTest {
    @Test
    public void complexLogicTest() {
        // Create a new world
        World world = new World("logic-gate-test", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "logic-gate-test" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // check that the light bulb is off
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        boolean light_off = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_off")) {
                light_off = true;
                break;
            }
        }
        // make sure the light is off
        assert(light_off);

        
        // move boulder and should still be off
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        light_off = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_off")) {
                light_off = true;
                break;
            }
        }
        // make sure the light is off
        assert(light_off);


        // move boulder and should be on
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        boolean light_on = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_on")) {
                light_on = true;
                break;
            }
        }
        // make sure the light is on
        assert(light_on);
    }
}
