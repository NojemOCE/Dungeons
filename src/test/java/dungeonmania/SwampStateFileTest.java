package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.DirectoryNotEmptyException;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

public class SwampStateFileTest {

    /**
     * When a boulder is pushed onto a swamp tile, check that the save and load game works as expected, and the boulder stays there for "movement_factor" ticks
     * MAP:
     * P S 
     *   B
     */
    @Test
    public void boulderSwampSaveTest() {

        DungeonManiaController c = new DungeonManiaController();

        c.newGame("simple_swamp", "standard");

        // move player into position to push boulder
        c.tick(null, Direction.DOWN);
        c.tick(null, Direction.DOWN);
        c.tick(null, Direction.RIGHT);

        // move up (pushing the boulder onto the swamp tile)
        c.tick(null, Direction.UP);

        // Save the game right after the boulder is moved onto the swamp tile
        assertDoesNotThrow(()-> c.saveGame("simple_swamp1"));
        
        // Move up 2x so that the boulder is pushed off the swamp
        c.tick(null, Direction.UP);
        c.tick(null, Direction.UP);

        // Load the game from the saved file 2 ticks prior
        DungeonResponse d = c.loadGame("simple_swamp1");


        // Check positions after load
        Position playerPos = null;
        Position boulderPos = null;
        List<EntityResponse> entities  = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);


        assertEquals(new Position(1, 1), playerPos);
        assertEquals(new Position(1, 0), boulderPos);

        // now check that the boulder is stuck there for another tick
        d = c.tick(null, Direction.UP);
        entities = d.getEntities();

        // get positions after tick
        Position playerPos2 = null;
        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
            }
        }
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);


        assertEquals(new Position(1, 1), playerPos2);
        assertEquals(new Position(1, 0), boulderPos2);


        // now check that after another tick, the boulder actually moves up
        d = c.tick(null, Direction.UP);
        entities = d.getEntities();

        // get positions after tick
        Position playerPos3 = null;
        Position boulderPos3 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos3 = er.getPosition();
            } else if (er.getType().equals("boulder")) {
                boulderPos3 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos3);
        assertNotNull(boulderPos3);


        assertEquals(playerPos2.translateBy(Direction.UP), playerPos3);
        assertEquals(boulderPos2.translateBy(Direction.UP), boulderPos3);
    }


    /**
     * Spider movement through swamp test
     * MAP:
     * W   W   player
     * W       swamp
     * W   W/s
     */
    @Test
    public void spiderSwampTest() {

        DungeonManiaController c = new DungeonManiaController();

        c.newGame("swamp+spider", "standard");
        
        // move spider into swamp tile
        c.tick(null, Direction.UP);
        c.tick(null, Direction.DOWN);

        assertDoesNotThrow(()-> c.saveGame("swamp+spider1"));
        
        //Make the spider move for 4 more ticks before we load the game to ensure it loads as expected
        c.tick(null, Direction.UP);
        c.tick(null, Direction.DOWN);
        c.tick(null, Direction.UP);
        c.tick(null, Direction.DOWN);

        // Load the saved file from 4 ticks prior
        DungeonResponse d = c.loadGame("swamp+spider1");
        // get the current positions
        Position spiderPos = null;
        Position swampPos = null;

        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("spider")) {
                spiderPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(spiderPos);
        assertNotNull(swampPos);

        // check they are on the same spot
        assertEquals(swampPos, spiderPos);

        // check they are in the same spot after a tick
        d = c.tick(null, Direction.NONE);
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("spider")) {
                spiderPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }
        // check that we got the positions
        assertNotNull(spiderPos);
        assertNotNull(swampPos);

        // check they are on the same spot
        assertEquals(swampPos, spiderPos);

        // now after one tick the spider should be at the next tile
        d = c.tick(null, Direction.NONE);

        Position spiderPos2 = null;
        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("spider")) {
                spiderPos2 = er.getPosition();
            }
        }

        // check that we got the position
        assertNotNull(spiderPos2);
        assertEquals(spiderPos.translateBy(Direction.DOWN), spiderPos2);
        
    }


    /**
     * Save file on mercenary movement through swamp test
     * MAP:
     * W   W   
     * W   M   swamp   player
     * W   W
     */
    @Test
    public void mercSwampTest() {

        DungeonManiaController c = new DungeonManiaController();

        c.newGame("swamp+merc", "standard");

        
        // move merc into swamp tile and immediately save file
        c.tick(null, Direction.RIGHT);
        assertDoesNotThrow(()-> c.saveGame("swamp+merc1"));

        //move for five more ticks
        c.tick(null, Direction.RIGHT);
        c.tick(null, Direction.RIGHT);
        c.tick(null, Direction.RIGHT);
        c.tick(null, Direction.RIGHT);
        c.tick(null, Direction.RIGHT);

        //load game from saved file 5 ticks prior
        DungeonResponse d = c.loadGame("swamp+merc1");


        // get the current positions
        Position mercPos = null;
        Position swampPos = null;

        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("mercenary") || er.getType().equals("assassin")){
                mercPos = er.getPosition();
            } else if (er.getType().equals("swamp_tile")) {
                swampPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos);
        assertNotNull(swampPos);

        // check the mercenary is still stuck on the swamp
        assertEquals(swampPos, mercPos);

        // check it is still on the same spot after one tick
        d = c.tick(null, Direction.LEFT);

        // get the current positions
        Position mercPos2 = null;

        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("mercenary") || er.getType().equals("assassin")) {
                mercPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos2);

        // check they are on the same spot
        assertEquals(mercPos, mercPos2);

        // now after one tick the merc will move right
        d = c.tick(null, Direction.RIGHT);
        

        Position mercPos3 = null;

        for (EntityResponse er : d.getEntities()) {
            if (er.getType().equals("mercenary")|| er.getType().equals("assassin")) {
                mercPos3 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(mercPos3);

        assertEquals(mercPos2.translateBy(Direction.RIGHT), mercPos3);
    }
}
