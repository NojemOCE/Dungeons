package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.movingEntity.*;
import dungeonmania.collectable.CollectableEntity;
import dungeonmania.collectable.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.Wall;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;


public class WorldTest {

    @ParameterizedTest
    @ValueSource(strings = {"advanced", "boulders", "maze"})
    public void testNewGame(String dungeon) {
        DungeonManiaController controller = new DungeonManiaController();
        World world;

        for (String mode : controller.getGameModes()) {
            world = new World(dungeon, mode, 1);
            assertEquals(dungeon, world.worldDungeonResponse().getDungeonName());
        }
    }

    @Test
    public void testDungeonResponse() {
        World world = new World("portals", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "portals" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        EntityResponse player = new EntityResponse("player1", "player", new Position(0, 0), false);
        EntityResponse portal1 = new EntityResponse("BLUE", "portal", new Position(1, 0), false);
        EntityResponse portal2 = new EntityResponse("BLUE2", "portal", new Position(4, 0), false);

        List<EntityResponse> entities = new ArrayList<>();
        entities.add(player);
        entities.add(portal1);
        entities.add(portal2);

        DungeonResponse expected  = new DungeonResponse("portals", "portals", entities, null, null, null);

        assertEquals(expected.getDungeonId(), world.worldDungeonResponse().getDungeonId());
        assertEquals(expected.getDungeonName(), world.worldDungeonResponse().getDungeonName());
        // Not sure why this throws error 

    }

    @Test
    public void testBuildable() {
        World world = new World("advanced", "Standard", 1);
        assertThrows(IllegalArgumentException.class, () -> world.build("invalid buildable"));

        assertThrows(InvalidActionException.class, () -> world.build("bow"));
        assertThrows(InvalidActionException.class, () -> world.build("shield"));

        // Need more tests to check it can be built after collecting necessary items
    }
    
}
