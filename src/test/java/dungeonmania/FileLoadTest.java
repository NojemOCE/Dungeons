package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FileLoadTest {

    @Test
    public void collectableWorldTest() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("collectable-world", "Standard");

        controller.saveGame("collectable-world1");

        
        String file;
        boolean saved = false;

        /*try { 
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            //ignore
        }*/
        
        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        List<String> games = controller.allGames();


        assertDoesNotThrow(() -> controller.loadGame("collectable-world1"));

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
        assertEquals("collectable-world1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("collectable-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);



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

        controller.saveGame("collectable-world2");

        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("collectable-world2"));

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
        assertEquals("collectable-world2", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("collectable-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);


    }
    


    @Test
    public void invalidSaveLoadTest() {
        DungeonManiaController controller = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, ()-> controller.saveGame("invalid game"));
        assertThrows(IllegalArgumentException.class, ()-> controller.loadGame("invalid game"));

        controller.newGame("boulders", "Standard");
        assertDoesNotThrow(()-> controller.saveGame("boulders1"));

        List<String> games = controller.allGames();

        assertDoesNotThrow(()-> controller.loadGame("boulders1"));
        
    }

    @Test
    public void newSavedFolderTest() {
        clearSavedFilesFolder();
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("boulders", "Standard");
        assertDoesNotThrow(()-> controller.saveGame("boulders1"));

        List<String> games = controller.allGames();

        assertDoesNotThrow(()-> controller.loadGame("boulders1"));
        
    }


    @Test
    public void staticsWorldTest1() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("statics-world", "Standard");
        
        controller.saveGame("statics-world1");

        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("statics-world1"));

        DungeonResponse d = controller.tick(null, Direction.UP);

        
        List <EntityResponse> entities = d.getEntities();
        assertEquals(29, entities.size());

        for (EntityResponse e: entities)  {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(1, 1)));
            }
            else if (e.getType().contains("portal")) {
                assert(e.getPosition().equals(new Position(2, 1)) || e.getPosition().equals(new Position(3, 3)));
            }
            else if (e.getType().equals("key")) {
                assert(e.getPosition().equals(new Position(2, 4)));
            }
            else if (e.getType().equals("door")) {
                assert(e.getPosition().equals(new Position(3, 4)));
            }
            else if (e.getType().equals("switch")) {
                assert(e.getPosition().equals(new Position(1, 4)));
            }
            else if (e.getType().equals("boulder")) {
                assert(e.getPosition().equals(new Position(1, 3)));
            }
            else if (e.getType().equals("exit")) {
                assert(e.getPosition().equals(new Position(4, 4)));
            }
            else if (e.getType().equals("zombie_toast_spawner")) {
                assert(e.getPosition().equals(new Position(4, 1)));
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
        assertEquals("statics-world1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("statics-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);

    }

    @Test
    public void staticsWorldTest2() {

        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("statics-world", "Standard");

        

        // Move player downwards
        DungeonResponse d = controller.tick(null, Direction.DOWN);
        
        // Move player downwards and push boulder onto switch
        controller.tick(null, Direction.DOWN);

        controller.tick(null, Direction.RIGHT);

        // Player collects key
        d = controller.tick(null, Direction.DOWN);

        assertEquals(1, d.getInventory().size());

        // Player opens door
        controller.tick(null, Direction.RIGHT);

        controller.saveGame("statics-world2");

       /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("statics-world2"));

        // Player moves down
        d = controller.tick(null, Direction.DOWN);

        List <EntityResponse> entities = d.getEntities();
        assertEquals(28, entities.size());

        for (EntityResponse e: entities)  {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(3, 4)));
            }
            else if (e.getType().contains("portal")) {
                assert(e.getPosition().equals(new Position(2, 1)) || e.getPosition().equals(new Position(3, 3)));
            }
            else if (e.getType().equals("door_open")) {
                assert(e.getPosition().equals(new Position(3, 4)));
            }
            else if (e.getType().equals("switch")) {
                assert(e.getPosition().equals(new Position(1, 4)));
            }
            else if (e.getType().equals("boulder")) {
                assert(e.getPosition().equals(new Position(1, 4)));
            }
            else if (e.getType().equals("exit")) {
                assert(e.getPosition().equals(new Position(4, 4)));
            }
            else if (e.getType().equals("zombie_toast_spawner")) {
                assert(e.getPosition().equals(new Position(4, 1)));
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
        assertEquals("statics-world2", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("statics-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);

    }

    @Test
    public void enemiesWorldTest1() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("enemies-world", "Standard");

        controller.saveGame("enemies-world1");

        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("enemies-world1"));

        DungeonResponse d = controller.tick(null, Direction.UP);

        
        List <EntityResponse> entities = d.getEntities();
        assertEquals(23, entities.size());

        for (EntityResponse e: entities) {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(1, 1)));
            }
            else if (e.getType().equals("bomb")) {
                assert(e.getPosition().equals(new Position(2, 1)));
            }
            else if (e.getType().equals("mercenary") || e.getType().equals("assassin")) {
                assert(e.getPosition().equals(new Position(2, 1)));
            }
            else if (e.getType().equals("sword")) {
                assert(e.getPosition().equals(new Position(1, 2)));
            }
            else if (e.getType().equals("zombie_toast")) {

                assert(Position.isAdjacent(e.getPosition(), new Position(1, 3)) || e.getPosition().equals(new Position(1,3)));
            }
            else if (e.getType().equals("spider")) {
                assert(e.getPosition().equals(new Position(3, 2)));
            }
            else if (e.getType().equals("treasure")) {
                assert(e.getPosition().equals(new Position(2, 2)));
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
        assertEquals("enemies-world1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("enemies-world", dungeonName);

        String goals = d.getGoals();
        assertEquals("(:enemies AND :treasure)", goals);
    }

    @Test
    public void placingBombWorldTest() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("placing-bomb", "Standard");

        DungeonResponse d = controller.tick(null, Direction.DOWN);
        List<ItemResponse> inventory = d.getInventory();

        String bombId = null;
        for (ItemResponse ir : inventory) {
            if (ir.getType().equals("bomb")) {
                bombId = ir.getId();
            }
        }
        assertNotNull(bombId);

        controller.tick(bombId, Direction.NONE);
        controller.tick(null, Direction.DOWN);

        controller.saveGame("placing-bomb1");

        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("placing-bomb1"));

        d = controller.tick(null, Direction.UP);
        
        List <EntityResponse> entities = d.getEntities();
        assertEquals(19, entities.size());

        for (EntityResponse e: entities) {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(1, 3)));
            }
            else if (e.getType().equals("bomb_placed")) {
                assert(e.getPosition().equals(new Position(1, 2)));
            }
            else if (e.getType().equals("treasure")) {
                assert(e.getPosition().equals(new Position(3, 3)));
            }
            else {
                assertEquals("wall", e.getType());
            }
        }

        inventory = d.getInventory();
        assertEquals(0, inventory.size());

        List<String> buildables = d.getBuildables();
        assertEquals(0, buildables.size());

        String dungeonID = d.getDungeonId();
        assertEquals("placing-bomb1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("placing-bomb", dungeonName);

        String goals = d.getGoals();
        assertEquals(":treasure", goals);
    }

    @Test
    public void buildingWorldTest() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("building-world", "Standard");

        // Move right and collect wood
        controller.tick(null, Direction.RIGHT);

        // Move right and collect wood
        controller.tick(null, Direction.RIGHT);

        // Move down and collect wood
        controller.tick(null, Direction.DOWN);

        // Move down and collect treasure
        controller.tick(null, Direction.DOWN);

        // Move left and collect arrow
        controller.tick(null, Direction.LEFT);

        // Move left and collect arrow
        controller.tick(null, Direction.LEFT);

        // Move up
        controller.tick(null, Direction.UP);

        // Move right and collect arrow
        controller.tick(null, Direction.RIGHT);

        controller.build("bow");
        controller.build("shield");


        controller.saveGame("building-world1");

        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        List<String> games = controller.allGames();

        assertDoesNotThrow(() -> controller.loadGame("building-world1"));

        DungeonResponse d = controller.tick(null, Direction.UP);
        
        List <EntityResponse> entities = d.getEntities();
        assertEquals(17, entities.size());

        for (EntityResponse e: entities) {
            if (e.getType().equals("player")) {
                assert(e.getPosition().equals(new Position(2, 1)));
            }
            else {
                assertEquals("wall", e.getType());
            }
        }

        List<ItemResponse> inventory = d.getInventory();
        assertEquals(2, inventory.size());
        boolean containsBow = false;
        boolean containsShield = false;

        for (ItemResponse i: inventory) {
            if (i.getType().equals("bow")) {
                containsBow = true;
            }
            if (i.getType().equals("shield")) {
                containsShield = true;
            }
        }

        assert(containsBow && containsShield);

        List<String> buildables = d.getBuildables();
        assertEquals(0, buildables.size());

        String dungeonID = d.getDungeonId();
        assertEquals("building-world1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("building-world", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);

    }

    @Test
    public void invincicibilityPotionWorldTest() {
        DungeonManiaController controller = new DungeonManiaController();

        controller.newGame("invinc-potion", "Standard");

        //Player collects invinc potion
        DungeonResponse d = controller.tick(null, Direction.RIGHT);

        String invincID = "";

        for (ItemResponse i  : d.getInventory()) {
            if (i.getType().equals("invincibility_potion")) {
                invincID = i.getId();
            }
        }

        controller.tick(invincID, Direction.NONE);
        controller.tick(null, Direction.LEFT);

        assertDoesNotThrow(()-> controller.saveGame("invinc-potion1"));
      
        /*try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
        List<String> games = controller.allGames();


        assertDoesNotThrow(() -> controller.loadGame("invinc-potion1"));

        d = controller.tick(null, Direction.DOWN);

        List <EntityResponse> entities = d.getEntities();

        List<ItemResponse> inventory = d.getInventory();
        assertEquals(0, inventory.size());

        List<String> buildables = d.getBuildables();
        assertEquals(0, buildables.size());

        String dungeonID = d.getDungeonId();
        assertEquals("invinc-potion1", dungeonID);

        String dungeonName = d.getDungeonName();
        assertEquals("invinc-potion", dungeonName);

        String goals = d.getGoals();
        assertNull(goals);



    }

    /**
     * MAP:
     * P  H
     * I
     */
    @Test
    public void activeInvisibilityPotionLoadTest() {
        clearSavedFilesFolder();
        DungeonManiaController c = new DungeonManiaController();

        c.newGame("health-invis", "standard");

        //collect invis potion
        DungeonResponse d = c.tick(null, Direction.DOWN);

        //Check invis potion is in inventory
        assertEquals(1, d.getInventory().size());
        String potionID = "";
        for (ItemResponse i : d.getInventory()) {
            assertEquals("invisibility_potion", i.getType());
            potionID = i.getId();
        }

        // save game with invis potion in inventory
        c.saveGame("health-invis1");

        //use the invisibility potion
        d = c.tick(potionID, Direction.NONE);
        assertEquals(0, d.getInventory().size());

        //load the saved game (before the potion was used)
        d = c.loadGame("health-invis1");
        assertEquals(1, d.getInventory().size());
        potionID = "";
        for (ItemResponse i : d.getInventory()) {
            assertEquals("invisibility_potion", i.getType());
            potionID = i.getId();
        }

        //use the invis potion
        d = c.tick(potionID, Direction.NONE);
        assertEquals(0, d.getInventory().size());

        // save game with invis potion active
        c.saveGame("health-invis2");

        // move down
        d = c.tick(null, Direction.DOWN);

        //load game to right after invis potion was used
        d  = c.loadGame("health-invis2");
        assertEquals(0, d.getInventory().size());

        clearSavedFilesFolder();
    }

    /**
     * MAP:
     * P  H
     * I
     */
    @Test
    public void activeHealthPotionLoadTest() {
        clearSavedFilesFolder();
        DungeonManiaController c = new DungeonManiaController();

        c.newGame("health-invis", "standard");

        //collect health potion
        DungeonResponse d = c.tick(null, Direction.RIGHT);

        //Check health potion is in inventory
        assertEquals(1, d.getInventory().size());
        String potionID = "";
        for (ItemResponse i : d.getInventory()) {
            assertEquals("health_potion", i.getType());
            potionID = i.getId();
        }

        // save game with health potion in inventory
        c.saveGame("health-invis1");

        //use the health potion
        d = c.tick(potionID, Direction.NONE);
        assertEquals(0, d.getInventory().size());

        //load the saved game (before the potion was used)
        d = c.loadGame("health-invis1");
        assertEquals(1, d.getInventory().size());
        potionID = "";
        for (ItemResponse i : d.getInventory()) {
            assertEquals("health_potion", i.getType());
            potionID = i.getId();
        }

        //use the invis potion
        d = c.tick(potionID, Direction.NONE);
        assertEquals(0, d.getInventory().size());

        // save game with invis potion active
        c.saveGame("health-invis2");

        // move down
        d = c.tick(null, Direction.DOWN);

        //load game to right after invis potion was used
        d  = c.loadGame("health-invis2");
        assertEquals(0, d.getInventory().size());
        
        clearSavedFilesFolder();
    }


    /**
     * Clears the saved files folder after each test
     */
    public void clearSavedFilesFolder() {
        deleteFile(new File("src/main/savedGames"));
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
