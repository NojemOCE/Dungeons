package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        List<String> games = controller.allGames();

        assertEquals(1, games.size());

        assertDoesNotThrow(() -> controller.loadGame(games.get(0)));

        DungeonResponse d = controller.tick(null, Direction.UP);

        List <EntityResponse> entities = d.getEntities();
        assertEquals(32, entities.size());

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
            else if (e.getType().equals("invincibility_potion")) {
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

        clearSavedFilesFolder();

    }

    @Test
    public void collectableWorldTest2() {
        DungeonManiaController controller = new DungeonManiaController();

        DungeonResponse d = controller.newGame("collectable-world", "Standard");
        List<ItemResponse> inv = d.getInventory();
        assertEquals(0, inv.size());

        // Player moves down and collects a health potion
        d = controller.tick(null, Direction.DOWN);
        inv = d.getInventory();
        assertEquals(1, inv.size());

        // Player moves down and collects an arrow
        d = controller.tick(null, Direction.DOWN);
        inv = d.getInventory();
        assertEquals(2, inv.size());

        // Player moves down and collects the one ring
        d = controller.tick(null, Direction.DOWN);
        inv = d.getInventory();
        assertEquals(3, inv.size());

        // Player moves right and collects nothing
        d = controller.tick(null, Direction.RIGHT);
        inv = d.getInventory();
        assertEquals(3, inv.size());

        // Player moves up and collects a bomb
        d = controller.tick(null, Direction.UP);
        inv = d.getInventory();
        assertEquals(4, inv.size());

        // Player moves right and collects a sword
        d = controller.tick(null, Direction.RIGHT);
        inv = d.getInventory();
        assertEquals(5, inv.size());

        // Player moves right and collects nothing
        d = controller.tick(null, Direction.RIGHT);
        inv = d.getInventory();
        assertEquals(5, inv.size());

        // Player moves up and collects wood
        d = controller.tick(null, Direction.UP);
        inv = d.getInventory();
        assertEquals(6, inv.size());

        // Player moves left and collects an invisibility potion
        d = controller.tick(null, Direction.LEFT);
        inv = d.getInventory();
        assertEquals(7, inv.size());

        // Player moves up and collects a key
        d = controller.tick(null, Direction.UP);
        inv = d.getInventory();
        assertEquals(8, inv.size());

        // Player moves left and collects treasure
        d = controller.tick(null, Direction.LEFT);
        inv = d.getInventory();
        assertEquals(9, inv.size());

        // Player moves down and collects an invincibility potion
        d = controller.tick(null, Direction.DOWN);
        inv = d.getInventory();
        assertEquals(10, inv.size());

        controller.saveGame("collectable-world");

        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        List<String> games = controller.allGames();

        assertEquals(1, games.size());

        assertDoesNotThrow(() -> controller.loadGame(games.get(0)));

        d = controller.tick(null, Direction.UP);

        List <EntityResponse> entities = d.getEntities();
        assertEquals(22, entities.size());

        inv = d.getInventory();
        assertEquals(10, inv.size());


        for (EntityResponse e: entities)  {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(2, 1)));
            }
            else if (e.getType().equals("door")) {
                assert(e.getPosition().equals(new Position(4, 1)));
            }
            else {
                assertEquals("wall", e.getType());
            }
        }

        List<String> buildables = d.getBuildables();
        assertEquals(0, buildables.size());

        String dungeonID = d.getDungeonId();
        assertEquals(games.get(0), dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("collectable-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);

        clearSavedFilesFolder();

    }
    


    @Test
    public void invalidSaveTest() {
        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, ()-> controller.saveGame("boulders"));

        controller.newGame("boulders", "Standard");

        assertThrows(IllegalArgumentException.class, ()-> controller.saveGame("advanced"));
    }



    /**
     * Clears the saved files folder after each test
     */
    public void clearSavedFilesFolder() {
        deleteFile(new File("src/main/resources/savedGames"));
    }

    /**
     * This takes a file path (or directory) and removes:
     * - the given file
     * - the directory and its sub contents
     * 
     * NOTE: this deleteFile method has been sourced from: 
     * https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java
     * We do not claim to be the original authors of this method.
     * @param path path to delete contents of
     */
    private void deleteFile(File path) {
        if (path.isDirectory()) {
            for (File sub : path.listFiles()) {
                deleteFile(sub);
            }
        }
        path.delete();
    }
}
