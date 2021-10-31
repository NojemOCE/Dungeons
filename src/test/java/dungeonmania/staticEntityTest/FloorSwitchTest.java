package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


public class FloorSwitchTest {
    @Test
    public void constructionTest(){
        Position floorSwitchPos = new Position(1,1);
        FloorSwitch floorSwitch = new FloorSwitch(floorSwitchPos.getX(), floorSwitchPos.getY(), "switch1");
        assertNotNull(floorSwitch);
        assert(floorSwitchPos.equals(floorSwitch.getPosition()));
    }

    /**
     * Testing that a player can stand on a switch
     * MAP:
     *  P B S
     */
    @Test
    public void playerOnSwitch(){
        // Create a new world
        World world = new World("player-boulder-switch", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "player-boulder-switch" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // move player next to switch
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.RIGHT);
        

        // get initial player position
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // now try and move on top of switch
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos2);

        assert(playerPos2.equals(playerPos.translateBy(Direction.UP)));

        // now try to walk off
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        assert(playerPos.equals(playerPos2.translateBy(Direction.UP)));
    }

}
