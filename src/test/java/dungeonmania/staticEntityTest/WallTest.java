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


public class WallTest {

    @Test
    public void constructionTest(){
        Position wallPos = new Position(1,1);
        Wall wall = new Wall(wallPos.getX(), wallPos.getY(), "wall1");
        assertNotNull(wall);
        assert(wallPos.equals(wall.getPosition()));
    }

    /**
     * Test that a player is unable to go through a wall
     */
    @Test
    public void walkIntoWall() {
        World world = new World("wall", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "wall" + ".json");
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

        // try to walk right - into the wall
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

        assert(playerPos.equals(playerPos2));
    }
}
