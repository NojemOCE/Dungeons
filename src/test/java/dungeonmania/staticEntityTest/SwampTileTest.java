package dungeonmania.staticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.DirectoryNotEmptyException;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    public void constructionTest(){
        Position swampPos = new Position(1,1);
        int movement_factor = 2;
        SwampTile swamp = new SwampTile(swampPos.getX(), swampPos.getY(), "swamp1",  movement_factor);
        assertNotNull(swamp);
        assert(swampPos.equals(swamp.getPosition()));
    }

    /**
     * Check that boulders can be pushed onto a swamp tile and stay there for "movement_factor" ticks
     * MAP:
     * P S 
     *   B
     */
    @Test
    public void boulderTest() {
        // Create a new world
        World world = new World("simple_swamp", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple_swamp" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move player into position to push boulder
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // get the current positions
        Position playerPos = null;
        Position boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);

        // move up and confirm player and boulder can both move up
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // get positions after tick
        Position playerPos2 = null;
        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);


        assert(playerPos2.equals(playerPos.translateBy(Direction.UP)));
        assert(boulderPos2.equals(boulderPos.translateBy(Direction.UP)));

        // now check that the boulder is stuck there for another tick
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // get positions after tick
        Position playerPos3 = null;
        Position boulderPos3 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos3 = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos3 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos3);
        assertNotNull(boulderPos3);


        assert(playerPos3.equals(playerPos2));
        assert(boulderPos3.equals(boulderPos2));

        // now check that after another tick, it is actually moved up
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // get positions after tick
        playerPos3 = null;
        boulderPos3 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos3 = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos3 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos3);
        assertNotNull(boulderPos3);


        assert(playerPos3.equals(playerPos2.translateBy(Direction.UP)));
        assert(boulderPos3.equals(boulderPos2.translateBy(Direction.UP)));
    }


    /**
     * Check that player can walk onto swamp tile and it DOES NOT take extra time
     * MAP:
     * P S 
     *   B
     */
    @Test
    public void playerMovementTest() {
        // Create a new world
        World world = new World("simple_swamp", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple_swamp" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move player into swamp tile
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // get the current positions
        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            }
        }

        // check that we got the position
        assertNotNull(playerPos);

        // movement factor is 2 but does not affect player
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        // get positions after tick
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);

        assert(playerPos2.equals(playerPos.translateBy(Direction.RIGHT)));
    }

    /**
     * Spider movement through swamp test
     * MAP:
     * W   W   player
     * W       swamp
     * W   W/s
     */
    @Test
    public void spiderSwampTest() {
        // Create a new world
        World world = new World("swamp+spider", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "swamp+spider" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move spider into swamp tile
        DungeonResponse d = world.tick(null, Direction.NONE);
        d = world.tick(null, Direction.NONE);
        List<EntityResponse> entities = d.getEntities();

        // get the current positions
        Position spiderPos = null;
        Position swampPos = null;

        for (EntityResponse er : entities) {
            if (er.getType().equals("spider")) {
                spiderPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(spiderPos);
        assertNotNull(swampPos);

        // check they are on the same spot
        assert(spiderPos.equals(swampPos));

        // check they are in the same spot after a tick
        d = world.tick(null, Direction.NONE);
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("spider")) {
                spiderPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }
        // check that we got the positions
        assertNotNull(spiderPos);
        assertNotNull(swampPos);

        assert(spiderPos.equals(swampPos));

        // now after one tick the spider should be at the next tile
        d = world.tick(null, Direction.NONE);
        entities = d.getEntities();
        Position spiderPos2 = null;

        for (EntityResponse er : entities) {
            if (er.getType().equals("spider")) {
                spiderPos2 = er.getPosition();
            }
        }

        // check that we got the position
        assertNotNull(spiderPos2);
        assert(spiderPos2.equals(spiderPos.translateBy(Direction.DOWN)));
        
    }


    /**
     * Mercenary movement through swamp test
     * MAP:
     * W   W   
     * W   M   swamp   player
     * W   W
     */
    @Test
    public void mercSwampTest() {
        // Create a new world
        World world = new World("swamp+merc", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "swamp+merc" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move merc into swamp tile
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // get the current positions
        Position mercPos = null;
        Position swampPos = null;

        for (EntityResponse er : entities) {
            if (er.getType().equals("mercenary") || er.getType().equals("assassin")){
                mercPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos);
        assertNotNull(swampPos);

        // check they are on the same spot
        assertEquals(swampPos, mercPos);

        // check it is still on the same spot after one tick
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        // get the current positions
        Position mercPos2 = null;

        for (EntityResponse er : entities) {
            if (er.getType().equals("mercenary") || er.getType().equals("assassin")) {
                mercPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos2);

        // check they are on the same spot
        assertEquals(mercPos, mercPos2);

        // now after one tick the merc will move right
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        // get the current positions
        //mercPos2 = null;

        Position mercPos3 = null;

        for (EntityResponse er : entities) {
            if (er.getType().equals("mercenary")|| er.getType().equals("assassin")) {
                mercPos3 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos3);

        assertEquals(mercPos2.translateBy(Direction.RIGHT), mercPos3);
    }
}
