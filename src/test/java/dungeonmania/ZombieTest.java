package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class ZombieTest {

    @Test
    public void zombieSpawnTest(){
        World world = new World("zombie-spawn-test", "Standard");
        
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "zombie-spawn-test" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        for (int i=0; i < 20; i++) {
            assertDoesNotThrow(()-> world.tick(null, Direction.UP));
        }

        DungeonResponse d = world.tick(null, Direction.UP);

        List<EntityResponse> es = d.getEntities();

        Boolean isZombie = false;
        int zombieCount = 0;
        for (EntityResponse e : es) {
            if (e.getType().equals("zombie_toast")) {
                isZombie = true;
                zombieCount++;
            }
        }

        assertEquals(true, isZombie);
        assertEquals(1, zombieCount);
    }

    @Test
    public void zombieInteractTest(){
        World world = new World("invalid-interact", "Standard");
        
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "invalid-interact" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(IllegalArgumentException.class, () -> world.interact("zombie_toast2"));

        assertThrows(IllegalArgumentException.class, () -> world.interact("wall3"));

    }


}