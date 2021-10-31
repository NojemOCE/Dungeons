package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.exceptions.InvalidActionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class FileLoadTest {


    @Test
    public void collectableWorldTest() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("collectable-world", "Standard");

        controller.saveGame("collectable-world");

        List<String> games = controller.allGames();

        assertEquals(1, games.size());

        assertDoesNotThrow(() -> controller.loadGame(games.get(0)));

        DungeonResponse d = controller.tick("null", Direction.UP);

        List <EntityResponse> entities = d.getEntities();
        assertEquals(33, entities.size());

        for (EntityResponse e: entities)  {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(1, 1)));
            }
            else if (e.getType().equals("treasure")) {
                assert(e.getPosition().equals(new Position(2, 1)));
            }
            else if (e.getType().equals("key")) {
                assert(e.getPosition().equals(new Position(3, 1)));
            }
            else if (e.getType().equals("door")) {
                assert(e.getPosition().equals(new Position(4, 1)));
            }
            else if (e.getType().equals("health_potion")) {
                assert(e.getPosition().equals(new Position(1, 2)));
            }
            else if (e.getType().equals("innvincibility_potion")) {
                assert(e.getPosition().equals(new Position(2, 2)));
            }
            else if (e.getType().equals("invisibility_potion")) {
                assert(e.getPosition().equals(new Position(3, 2)));
            }
            else if (e.getType().equals("wood")) {
                assert(e.getPosition().equals(new Position(4, 2)));
            }
            else if (e.getType().equals("arrow")) {
                assert(e.getPosition().equals(new Position(1, 3)));
            }
            else if (e.getType().equals("bomb")) {
                assert(e.getPosition().equals(new Position(2, 3)));
            }
            else if (e.getType().equals("sword")) {
                assert(e.getPosition().equals(new Position(3, 3)));
            }
            else if (e.getType().equals("armour")) {
                assert(e.getPosition().equals(new Position(4, 3)));
            }
            else if (e.getType().equals("one_ring")) {
                assert(e.getPosition().equals(new Position(1, 4)));
            }
            else {
                assertEquals("wall", e.getType());
            }
        }

        List<ItemResponse> inventory = d.getInventory();
        assertEquals(0, inventory.size());

        List<String> buildables = d.getBuildables();
        assertEquals(0, buildables.size());

        String dungeonID = d.getDungeonId();
        assertEquals(games.get(0), dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("collectable-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);


    }


    @Test
    public void invalidSaveTest() {
        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, ()-> controller.saveGame("boulders"));

        controller.newGame("boulders", "Standard");

        assertThrows(IllegalArgumentException.class, ()-> controller.saveGame("advanced"));
    }
    
}
