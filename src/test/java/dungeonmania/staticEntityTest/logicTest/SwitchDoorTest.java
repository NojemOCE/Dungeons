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
import dungeonmania.staticEntity.SwitchDoor;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class SwitchDoorTest {
    @Test
    public void constructionTest(){
        Position doorPos = new Position(1, 1);
        SwitchDoor door = new SwitchDoor(doorPos.getX(), doorPos.getY(), "switch_door1", new OrLogic());
        assertNotNull(door);
        assert(doorPos.equals(door.getPosition()));
    }

    /**
     * Test that door switch works as expected
     * MAP:
     * P  D
     * B  W
     * S  W 
     */
    @Test
    public void basicTest() {
        // Create a new world
        World world = new World("switch_door", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "switch_door" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // make sure we can't walk into locked door
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        // initial pos
        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        // make sure we find it
        assertNotNull(playerPos);

        // now try to walk right (into the door)
        // should not change spots because we dont have a key
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // check that player doesn't move
        assert(playerPos.equals(playerPos2));

        // now activate switch and check that the door has opened
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        boolean door_open = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("switch_door_open")) {
                door_open = true;
                break;
            }
        }
        
        assert(door_open);

        // now move boulder off switch and check door closes

        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        boolean door_closed = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("switch_door")) {
                door_closed = true;
                break;
            }
        }
        
        assert(door_closed);

        // try to walk into it again
        d = world.tick(null, Direction.UP);
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // initial pos
        playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        // make sure we find it
        assertNotNull(playerPos);

        // now try to walk right (into the door)
        // should not change spots because we dont have a key
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // check that player doesn't move
        assert(playerPos.equals(playerPos2));

    }

    /**
     * Test that we can open and walk through switch door
     * MAP:
     * P  D
     * B  W
     * S  W 
     */
    @Test
    public void openWalkThrough() {
        // Create a new world
        World world = new World("switch_door", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "switch_door" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // make sure we can't walk into locked door
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        // initial pos
        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        // make sure we find it
        assertNotNull(playerPos);

        // now try to walk right (into the door)
        // should not change spots because we dont have a key
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // check that player doesn't move
        assert(playerPos.equals(playerPos2));

        // now activate switch and check that the door has opened
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        boolean door_open = false;
        for (EntityResponse er : entities) {
            if (er.getType().equals("switch_door_open")) {
                door_open = true;
                break;
            }
        }
        
        assert(door_open);

        // now try to walk though door
        d = world.tick(null, Direction.UP);     
        entities = d.getEntities();

        // initial pos
        playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        // make sure we find it
        assertNotNull(playerPos);

        // now try to walk right (into the door)
        // should not change spots because we dont have a key
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // check that player doesn't move
        assert(playerPos2.equals(playerPos.translateBy(Direction.RIGHT)));
    }
  
  
}
