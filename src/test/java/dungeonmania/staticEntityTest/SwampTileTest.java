package dungeonmania.staticEntityTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
     * Check that boulders can be pushed onto a swamp tile
     * MAP:
     * P S 
     *   B
     */
    @Test
    public void boulderTest() {
        // Create a new world
        World world = new World("simple_swamp", "Standard");
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
        World world = new World("simple_swamp", "Standard");
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


    // TODO: force enemies to walk on the swamp to test movement and make sure they are also slowed down
}
