package dungeonmania.movingEntityTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static dungeonmania.TestHelpers.assertListAreEqualIgnoringOrder;

public class SpiderTest {

    @Test
    public void spiderTest1(){
        World world = new World("maze", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "maze" + ".json");
            
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

        Boolean isSpider = false;
        int spiderCount = 0;
        for (EntityResponse e : es) {
            if (e.getType().equals("spider")) {
                isSpider = true;
                spiderCount++;
            }
        }

        assertEquals(true, isSpider);
        assertEquals(1, spiderCount);
    }

    @Test
    public void preplacedSpiderTest(){
        // Loads a board with the maximum number of spiders already placed on it.
        // Character is moved to ensure that they don't move into any of these spiders.
        // Spider placement has been selected to ensure that they do not run into each other
        World world = new World("simple-spiders", "Standard");
        
        DungeonResponse d = null;
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spiders" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            d = world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Boolean isSpider = false;
        int spiderCount = 0;
        List<String> spiderIDs = new ArrayList<>();

        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                isSpider = true;
                spiderCount++;
                spiderIDs.add(e.getId());
            }
        }

        assertEquals(true, isSpider);
        assertEquals(6, spiderCount);

        

        for (int i=0; i < 20; i++) {
            assertDoesNotThrow(()-> world.tick(null, Direction.UP));
        }

        d = world.tick(null, Direction.UP);

        List<EntityResponse> es = d.getEntities();

        isSpider = false;
        Boolean newSpider = false;
        spiderCount = 0;
        //Check after 20 ticks that the number of spiders on the board is still 6 and that no new spiders have spawned
        for (EntityResponse e : es) {
            if (e.getType().equals("spider")) {
                isSpider = true;
                spiderCount++;
                if (!(spiderIDs.contains(e.getId()))){
                    newSpider = true;
                }
            }
        }

        assertEquals(false, newSpider);
        assertEquals(true, isSpider);
        assertEquals(6, spiderCount);

    }

    @Test
    public void battlePreplacedSpiderTest(){
        // Loads a board with the maximum number of spiders already placed on it.
        // Character is moved to ensure that they don't move into any of these spiders.
        // Spider placement has been selected to ensure that they do not run into each other
        World world = new World("simple-spiders", "Standard");
        
        DungeonResponse d = null;
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spiders" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            d = world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Boolean isSpider = false;
        int spiderCount = 0;
        List<String> spiderIDs = new ArrayList<>();

        // Check initial spiders
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                isSpider = true;
                spiderCount++;
                spiderIDs.add(e.getId());
            }
        }
        assertEquals(true, isSpider);
        assertEquals(6, spiderCount);

        assertDoesNotThrow(()-> world.tick(null, Direction.DOWN));

        d = world.tick(null, Direction.DOWN);

        spiderCount = 0;
        // Check one spider has been killed
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                spiderCount++;
            }
        }
        assertEquals(5, spiderCount);

        for (int i=0; i < 18; i++) {
            assertDoesNotThrow(()-> world.tick(null, Direction.UP));
        }

        d = world.tick(null, Direction.UP);

        List<EntityResponse> es = d.getEntities();

        isSpider = false;
        Boolean newSpider = false;
        int newSpiderCount = 0;
        spiderCount = 0;
        //Check that one new spider has spawned
        for (EntityResponse e : es) {
            if (e.getType().equals("spider")) {
                isSpider = true;
                spiderCount++;
                if (!(spiderIDs.contains(e.getId()))){
                    newSpider = true;
                    newSpiderCount = 1;
                }
            }
        }

        assertEquals(true, newSpider);
        assertEquals(1, newSpiderCount);
        assertEquals(true, isSpider);
        assertEquals(6, spiderCount);

    }

    @Test
    public void spiderBoulderTest1() {
        World world = new World("simple-spider-boulder1", "Standard");
        DungeonResponse d = null;
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-boulder1" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            d = world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Position p = null;
        int spiderCount = 0;
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                p = e.getPosition();
                spiderCount++;
            }
        }

        Position exp = new Position(2,3);
        assertEquals(1, spiderCount);
        assertEquals(exp, p);
        

        d = world.tick(null, Direction.UP);

        p = null;
        spiderCount = 0;
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                p = e.getPosition();
                spiderCount++;
            }
        }

        exp = exp.translateBy(Direction.UP);
        assertEquals(1, spiderCount);
        assertEquals(exp, p);


        d = world.tick(null, Direction.UP);
        // Assert spider has moved up one square. Note that the spider is currently in between two boulders.
        p = null;
        spiderCount = 0;
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                p = e.getPosition();
                spiderCount++;
            }
        }

        assertEquals(1, spiderCount);
        assertEquals(exp, p);

        d = world.tick(null, Direction.UP);

        p = null;
        spiderCount = 0;
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                p = e.getPosition();
                spiderCount++;
            }
        }

        assertEquals(1, spiderCount);
        assertEquals(exp, p);

    }

    @Test
    public void spiderBoulderTest2() {
        World world = new World("simple-spider-boulder2", "Standard");
        DungeonResponse d = null;
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-boulder2" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            d = world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Check initial position of the spider is (2,3)
        Position p = null;
        int spiderCount = 0;
        String spiderID  = "";
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                spiderCount++;
                spiderID = e.getId();

            }
        }

        p =  getSpiderPosition(d.getEntities(), spiderID);

        Position exp = new Position(2,3);

        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move up on this tick
        d = world.tick(null, Direction.UP);
        
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.UP);

        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move right on this tick
        d = world.tick(null, Direction.UP);
        
        p =  getSpiderPosition(d.getEntities(), spiderID);
        
        exp = exp.translateBy(Direction.RIGHT);

        assertNotNull(p);
        assertEquals(exp, p);


        // Spider should stay put on this tick
        d = world.tick(null, Direction.UP);
        
        p =  getSpiderPosition(d.getEntities(), spiderID);

        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should start moving left on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.LEFT);

        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should start moving left on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.LEFT);

        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should start stay put on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should start moving right on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.RIGHT);

        assertNotNull(p);
        assertEquals(exp, p);
    }

    @Test
    public void spiderBoulderTest3() {
        World world = new World("simple-spider-boulder3", "Standard");
        DungeonResponse d = null;
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "simple-spider-boulder3" + ".json");
            
            JSONObject game = new JSONObject(file);
            
            d = world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Check initial position of the spider is (2,3)
        Position p = null;
        int spiderCount = 0;
        String spiderID  = "";
        for (EntityResponse e : d.getEntities()) {
            if (e.getType().equals("spider")) {
                spiderCount++;
                spiderID = e.getId();

            }
        }

        p =  getSpiderPosition(d.getEntities(), spiderID);

        Position exp = new Position(2,3);

        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move up on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.UP);
        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move right on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.RIGHT);
        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move down on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.DOWN);
        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move down on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.DOWN);
        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move left on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.LEFT);
        assertNotNull(p);
        assertEquals(exp, p);

        // Spider should move left on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.LEFT);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should stay put on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move right on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.RIGHT);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move right on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.RIGHT);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move up on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.UP);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move up on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.UP);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move left on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.LEFT);
        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should stay put on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        assertNotNull(p);
        assertEquals(exp, p);
        
        // Spider should move right on this tick
        d = world.tick(null, Direction.UP);
        p =  getSpiderPosition(d.getEntities(), spiderID);

        exp = exp.translateBy(Direction.RIGHT);
        assertNotNull(p);
        assertEquals(exp, p);
    }

    private Position getSpiderPosition(List<EntityResponse> entities, String id) {
        for (EntityResponse e : entities) {
            if (e.getType().equals("spider") && id.equals(e.getId())) {
                return e.getPosition();
            }
        }
        return null;
    }
    
}
