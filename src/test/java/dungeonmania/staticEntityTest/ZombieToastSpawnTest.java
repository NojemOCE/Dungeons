package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.movingEntity.HealthPoint;
import dungeonmania.movingEntity.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.TestHelpers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;



public class ZombieToastSpawnTest {
    @Test
    public void constructionTest(){

        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(zombieSpawnPos.getX(), zombieSpawnPos.getY(), "zombie_toast_spawner1");
        assertNotNull(zombieSpawn);
        assert(zombieSpawnPos.equals(zombieSpawn.getPosition()));

    }

    /**
     * Test that spawn only returns cardinally adjacent cells
     */
    @Test
    public void checkSpawnAdjacent(){
        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(zombieSpawnPos.getX(), zombieSpawnPos.getY(), "zombie_toast_spawner1");
        List<Position> positions = zombieSpawn.spawn();
        
        // there should only be four cardinally adjacent spots
        assert(positions.size() == 4);

        // they need to all be adjacent
        for (Position p : positions) {
            assertTrue(Position.isAdjacent(p, zombieSpawn.getPosition()));
        }

    }

    /**
     * Test interactable status
     */
    @Test
    public void testInteractableStatus() {
        Position zombieSpawnPos = new Position(1,1);
        ZombieToastSpawn zombieSpawn = new ZombieToastSpawn(zombieSpawnPos.getX(), zombieSpawnPos.getY(), "zombie_toast_spawner1");

        HealthPoint hp = new HealthPoint(20);
        Player playerAdjacent = new Player(1, 0, "p1", hp);

        // check adjacent play results in interactable status
        zombieSpawn.update(playerAdjacent);
        EntityResponse er = zombieSpawn.getEntityResponse();
        assertTrue(er.isInteractable());

        // even if we are far away, the toaster should still be interactable
        Player playerFar = new Player(10, 0, "p2", hp);
        zombieSpawn.update(playerFar);
        er = zombieSpawn.getEntityResponse();
        assert(er.isInteractable());
    }

    /**
     * Test interact requires a weapon
     */
    @Test
    public void testInteract() {
        // Create a new world
        World world = new World("zombieSpawner+sword", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "zombieSpawner+sword" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        // check that the spawner is not interactable when we start (2 spots away)
        // should be "iteractable" but throws exception when interacted with
        for (EntityResponse er : entities) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assert(er.isInteractable());
                assertThrows(InvalidActionException.class, () -> world.interact(er.getId()));
                break;
            }
        }

        // walk closer and try again
        // should now be interactable but get invalid argument

        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assertTrue(er.isInteractable());
                assertThrows(InvalidActionException.class, () -> world.interact(er.getId()));
                break;
            }
        }
        
        // no go get weapon and try again
        // should be interactable and can destroy
        world.tick(null, Direction.DOWN);
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("zombie_toast_spawner")) {
                assertTrue(er.isInteractable());
                assertDoesNotThrow(() -> world.interact(er.getId()));
                break;
            }
        }

        // make sure it is destroyed (updates in the next tick)
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            assertFalse(er.getType().equals("zombie_toast_spawner"));
        }
    }


    /**
     * Test acts like a wall if not interacting
     */
    @Test
    public void testWalkingInto() {
        // Create a new world
        World world = new World("zombieSpawner+sword", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "zombieSpawner+sword" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // walk up to the spawner
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // get player position
        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // now walk into the toaster and make sure position doesnt change
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
