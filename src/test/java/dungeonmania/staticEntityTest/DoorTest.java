package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


public class DoorTest {
    @Test
    public void constructionTest(){
        Position doorPos = new Position(1, 1);
        int keyColour = 1;
        Door door = new Door(doorPos.getX(), doorPos.getX(), "door1", keyColour);
        assertNotNull(door);
        assert(doorPos.equals(door.getPosition()));
    }

    /**
     * Test that door can't be opened without key
     * MAP
     *  player door key
     */
    @Test
    public void keyOpenTest(){
        World world = new World("key+door", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "key+door" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // get the initial position
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

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

        // now go collect key and try again
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // check we have the key
        assertNotNull(world.keyInInventory(1));

        // now check that we can walk into the door
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // make sure they are on door (i.e. it is opened)
        assert(playerPos2.equals(playerPos.translateBy(Direction.RIGHT)));
    }

    /**
     * Test that door can't be opened with wrong key
     * MAP - key and door have mismatched keyColours
     *  player door key
     */
    @Test
    public void keyNoMatchTest(){
        World world = new World("key+door-noMatch", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "key+door-noMatch" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // get the initial position
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

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

        // now go collect key and try again
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // check we have the key
        assertNotNull(world.keyInInventory(2));

        // get the current position
        playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // now check that we CANT walk into the door
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        // make sure player doesn't move (can't open door)
        assert(playerPos2.equals(playerPos));
    }
}
