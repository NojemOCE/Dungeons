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

public class ExitTest {
    @Test
    public void constructionTest(){
        Position exitPos = new Position(1,1);
        Exit exit = new Exit(exitPos.getX(), exitPos.getY(), "exit1");
        assertNotNull(exit);
        assert(exitPos.equals(exit.getPosition()));
    }

    /**
     * Test that a player can walk on it
     * MAP:
     *  P E B
     */
    @Test
    public void playerInteract(){
        World world = new World("exit+boulder", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "exit+boulder" + ".json");
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

        // if we tick to the right we expect to move on top of the exit
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

        assert(playerPos2.equals(playerPos.translateBy(Direction.RIGHT)));
    }

    /**
     * Test that a we can roll a boulder on exit
     * MAP:
     *  P E B
     */
    @Test
    public void boulderInteract(){
        World world = new World("exit+boulder", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "exit+boulder" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move player to position to push boulder
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.RIGHT);

        // get the initial position of boulder
        DungeonResponse d = world.tick(null, Direction.UP);
        List<EntityResponse> entities = d.getEntities();

        Position boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
                break;
            }
        }
        // make sure we find it
        assertNotNull(boulderPos);

        // if we tick to the left we expect to move on top of the exit
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(boulderPos2);

        assert(boulderPos2.equals(boulderPos.translateBy(Direction.LEFT)));
    }
}
