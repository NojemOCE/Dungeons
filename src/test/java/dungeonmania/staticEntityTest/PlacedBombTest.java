package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.logic.OrLogic;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


public class PlacedBombTest {
    @Test
    public void constructionTest(){
        // Constructing a boulder
        Position bombPos = new Position(1, 1);
        PlacedBomb bomb = new PlacedBomb(bombPos.getX(), bombPos.getY(), "bomb1", new OrLogic());
        assertNotNull(bomb);
        assert(bombPos.equals(bomb.getPosition()));
    }

    /**
     * Test collecting and placing bomb
     */
    @Test
    public void testPlaceBomb() {
        // Create a new world
        World world = new World("player-bomb", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "player-bomb" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // move to the right and pick up the bomb
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();
        List<ItemResponse> inventory = world.getInventoryResponse();

        String bombId = null;
        for (ItemResponse ir : inventory) {
            if (ir.getType().equals("bomb")) {
                bombId = ir.getId();
            }
        }

        // place the bomb down
        d = world.tick(bombId, Direction.NONE);

        // now if we move away, it will act like a wall
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();

        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            }
        }
        assertNotNull(playerPos);
        
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            }
        }
        assertNotNull(playerPos2);

        assert(playerPos.equals(playerPos2));



        // now try to detonate
        // move player
        world.tick(null, Direction.LEFT);
        world.tick(null, Direction.UP);
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        // now check that everything in the one block radius is destroyed

        for (int i = 2; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                assert(world.getStaticEntitiesAtPosition(new Position(i, j)).size() == 0);
            }
        }
    }
}
