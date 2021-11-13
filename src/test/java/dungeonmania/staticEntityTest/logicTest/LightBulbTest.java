package dungeonmania.staticEntityTest.logicTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.logic.OrLogic;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.LightBulb;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class LightBulbTest {
    @Test
    public void constructionTest(){
        Position lightPos = new Position(1, 1);
        LightBulb lightbulb = new LightBulb(lightPos.getX(), lightPos.getY(), "light_bulb1", new OrLogic());
        assertNotNull(lightbulb);
        assert(lightPos.equals(lightbulb.getPosition()));
    }

    /**
     * Test that light bulb logic working
     * MAP:
     * P     B
     *    W  S
     *       L - (no logic)
     */
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
        
        // move boulder into position to push boulder
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        DungeonResponse d = world.tick(null, Direction.RIGHT);
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

        // now if we push teh boulder the light should be true
        d = world.tick(null, Direction.DOWN);
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

    /**
     * Test that light bulb logic working with "and" logic
     * MAP:
     * P      B
     *    W   S
     *    L&  W 
     */
    @Test
    public void andLightTest() {
        // Create a new world
        World world = new World("and-light", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "and-xor-light" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // move boulder into position to push boulder
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        DungeonResponse d = world.tick(null, Direction.RIGHT);
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

        // now if we push teh boulder the light should be true
        d = world.tick(null, Direction.DOWN);
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

    /**
     * Test that light bulb logic working with "xor" logic
     * MAP:
     * P      B     B
     *    W   S     S
     *        W     W     B
     *             L(x) W S
     */
    @Test
    public void xorLightTest() {
        // Create a new world
        World world = new World("xor-light", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "and-xor-light" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // move boulder into position to push boulder
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        DungeonResponse d = world.tick(null, Direction.RIGHT);
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

        // now if we push teh boulder the light should be true
        d = world.tick(null, Direction.DOWN);
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

        // now if we trigger the other switch the light should turn off
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.DOWN);
        d = world.tick(null, Direction.DOWN);
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

        // but if we take boulder off it turns back on
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        light_on = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_on")) {
                light_on = true;
                break;
            }
        }
        // make sure the light is on
        assert(light_on);
    }

    /**
     * Test that light bulb logic working with "not" logic
     * MAP:
     * P  B  S  L(not)
     */
    @Test
    public void notLightTest() {
        // Create a new world
        World world = new World("not-light", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "not-light" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();
        
        boolean light_on = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_on")) {
                light_on = true;
                break;
            }
        }
        // make sure the light is on
        assert(light_on);

        // push boulder and light should turn off
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        boolean light_off = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("light_bulb_off")) {
                light_off = true;
                break;
            }
        }
        // make sure the light is off
        assert(light_off);
    }
}
