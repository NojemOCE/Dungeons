package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;



public class BoulderTest {
    @Test
    public void constructionTest(){
        // Constructing a boulder
        Position boulderPos = new Position(1, 1);
        Boulder boulder = new Boulder(boulderPos.getX(), boulderPos.getY(), "boulder1");
        assertNotNull(boulder);
        assert(boulderPos.equals(boulder.getPosition()));
    }

    /**
     * Testing move function in boulder with player 
     * MAP
     *  P*B
     * There is a player with a spot between it and a boulder
     */
    @Test
    public void boulderMovePlayerLeft(){
        // Create a new world
        World world = new World("boulder+player", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "boulder+player" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // tick and the player should now be next to boulder
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        // Player is to the left of the boulder, so if we move right we expect them both to go right
        List<EntityResponse> entities = d.getEntities();

        // get the current positions
        Position playerPos = null;
        Position boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);


        // tick right and we want to check that the positions are both moved to the right
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        // get positions after tick
        Position playerPos2 = null;
        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);

        //confirm they are both moved one spot to the right
        assert(playerPos2.equals(playerPos.translateBy(Direction.RIGHT)));
        assert(boulderPos2.equals(boulderPos.translateBy(Direction.RIGHT)));



        // now check that we can push from the bottom
        
        // move player to under boulder
        d = world.tick(null, Direction.DOWN);
        d = world.tick(null, Direction.RIGHT);

        // get current positions
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);

        // now try to move them both up
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();
        
        // get positions after tick
        playerPos2 = null;
        boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);

        //confirm they are both moved one spot up
        assert(playerPos2.equals(playerPos.translateBy(Direction.UP)));
        assert(boulderPos2.equals(boulderPos.translateBy(Direction.UP)));



        // now check that we can push from the right
        
        // move player to the right
        d = world.tick(null, Direction.RIGHT);
        d = world.tick(null, Direction.UP);

        // get current positions
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);

        // now try to move them both left
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();
        
        // get positions after tick
        playerPos2 = null;
        boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);

        //confirm they are both moved one spot to the right
        assert(playerPos2.equals(playerPos.translateBy(Direction.LEFT)));
        assert(boulderPos2.equals(boulderPos.translateBy(Direction.LEFT)));
        



        // finally check that we can push from the top
        
        // move player to under boulder
        d = world.tick(null, Direction.UP);
        d = world.tick(null, Direction.LEFT);

        // get current positions
        entities = d.getEntities();
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
            } else {
                boulderPos = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos);
        assertNotNull(boulderPos);

        // now try to move them both down
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();
        
        // get positions after tick
        playerPos2 = null;
        boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
            } else {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(playerPos2);
        assertNotNull(boulderPos2);

        //confirm they are both moved one spot up
        assert(playerPos2.equals(playerPos.translateBy(Direction.DOWN)));
        assert(boulderPos2.equals(boulderPos.translateBy(Direction.DOWN)));
    }

    /**
     * Test that boulders can be pushed through portals
     * MAP
     * 
     * player B _ P _ _ P
     *  
     */ 
    @Test
    public void boulderMoveThroughPortal() {
        // Create a new world
        World world = new World("portals-boulder", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "portals-boulder" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // tick and the player should now be next to boulder next to a portal
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // if we move right again, we expect the boulder to move to the right side of the second portal

        // get the current position
        Position boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
                break;
            }
        }

        assertNotNull(boulderPos);

        // now tick once and check that it has moved to the right by 5 spots
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        // get position after tick
        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
            }
        }

        // check that we got the positions
        assertNotNull(boulderPos2);

        //confirm they are both moved five spots to the right (teleported)
        assert(boulderPos2.equals(boulderPos.translateBy(5, 0)));


        // now try to move the boulder back 
        // move player to the other side of the boulder
        world.tick(null, Direction.DOWN);
        for (int i = 0; i < 6; i++) {
            world.tick(null, Direction.RIGHT);
        }
        world.tick(null, Direction.UP);
        d = world.tick(null, Direction.LEFT);

        entities = d.getEntities();

        // get the boulder pos
        Position boulderPos3 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos3 = er.getPosition();
                break;
            }
        }

        assertNotNull(boulderPos3);
        // should be equal to first position

        assert(boulderPos.equals(boulderPos3));

        // now test travel from the top
        world.tick(null, Direction.DOWN);
        for (int i = 0; i < 5; i++) {
            world.tick(null, Direction.LEFT);
        }

        // move player
        world.tick(null, Direction.UP);
        world.tick(null, Direction.LEFT);
        world.tick(null, Direction.UP);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.UP);
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        // get top pos
        boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
                break;
            }
        }

        assertNotNull(boulderPos);

        // now if we push down we want it to teleport
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        // get pos
        boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
                break;
            }
        }

        assertNotNull(boulderPos2);
        // check that it is moved down 2, right 3
        assert(boulderPos2.equals(boulderPos.translateBy(3, 2)));


        // finally push the boulder up
        // move player
        world.tick(null, Direction.LEFT);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.RIGHT);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.DOWN);
        world.tick(null, Direction.LEFT);
        
        // push up
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();

        // get pos
        boulderPos3 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos3 = er.getPosition();
                break;
            }
        }

        // check that this pos is equal to boulderPos (before we moved portal last)
        assert(boulderPos.equals(boulderPos3));
    }

    // test can't move two boulders

    /**
     * Testing that we can't move two boulders
     * MAP
     *  P*B
     * There is a player with a spot between it and a boulder
     */
    @Test
    public void twoBoulderTest(){
        // Create a new world
        World world = new World("boulders", "Standard");
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "boulders" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
     
        // set up world such that there will be two boulders in the way
        world.tick(null, Direction.RIGHT);
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        // get current player position
        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }

        assertNotNull(playerPos);

        // now try to push two boulders
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        // check that new position is the same
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

    /**
     * Testing that boulder be rolled on and off switches
     * MAP:
     *  P B S
     */
    @Test
    public void canRollOnSwitch(){
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
        
        // get initial boulder position
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        Position boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
                break;
            }
        }
        assertNotNull(boulderPos);

        // now roll and check it can go on switch
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        Position boulderPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos2 = er.getPosition();
                break;
            }
        }
        assertNotNull(boulderPos2);

        assert(boulderPos2.equals(boulderPos.translateBy(Direction.RIGHT)));

        // now try to roll it off
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();

        boulderPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("boulder")) {
                boulderPos = er.getPosition();
                break;
            }
        }
        assertNotNull(boulderPos);

        assert(boulderPos.equals(boulderPos2.translateBy(Direction.RIGHT)));
    }

}
