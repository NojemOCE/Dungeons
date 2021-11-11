package dungeonmania.staticEntityTest;


import dungeonmania.World;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class PortalTest {
    @Test
    public void constructionTest(){

        Position portalPos = new Position(1,1);
        Portal portal = new Portal(portalPos.getX(), portalPos.getY(), "portal1", "BLUE");
        assertNotNull(portal);
        assert(portalPos.equals(portal.getPosition()));
    
    }

    /**
     * Test that twin portal construction works as expected
     */
    @Test
    public void assignTwinPortal(){
        Position portalPos = new Position(1,1);
        Portal portal = new Portal(portalPos.getX(), portalPos.getY(), "portal1", "BLUE");
        assertNull(portal.getTwinPortal());

        Position twinPortalPos = new Position(1,5);
        Portal twinPortal = new Portal(twinPortalPos.getX(), twinPortalPos.getY(), "portal2", "BLUE", portal);
        assertNotNull(twinPortal);
        assert(twinPortalPos.equals(twinPortal.getPosition()));

        // check twins are set correctly
        assert(twinPortal.getId().equals(portal.getTwinPortal().getId()));
        assert(portal.getId().equals(twinPortal.getTwinPortal().getId()));    
    }

    /**
     * Test that player ends up on the correct side of the twin portal
     * MAP:
     * player P _ _ P
     */
    @Test
    public void twinTeleport() {
        // Create a new world
        World world = new World("portals", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "portals" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // get initial player position
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // move right into portal
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        // get positions after tick
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }

        // check it has teleport to the right side of twin
        assert(playerPos2.equals(playerPos.translateBy(5, 0)));

        // move back
        d = world.tick(null, Direction.LEFT);
        entities = d.getEntities();
        
        // get positions after tick
        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }

        assert(playerPos.equals(playerPos2));

        // move player to enter from bottom
        world.tick(null, Direction.DOWN);
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        // get initial position
        playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }

        // now move up
        d = world.tick(null, Direction.UP);
        entities = d.getEntities();
        
        // get positions after tick
        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }

        assert(playerPos2.equals(playerPos.translateBy(3, -2)));

        // finally move back down
        d = world.tick(null, Direction.DOWN);
        entities = d.getEntities();

        // get positions after tick
        playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }

        assert(playerPos.equals(playerPos2));
    }

    /**
     * Test that if the other side of the twin portal is blocked, the player can't go through
     * MAP:
     * player B P _ _ P _ B
     */
    @Test
    public void twinTeleportBlock() {
        // Create a new world
        World world = new World("portals-two-boulders-v2", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "portals-two-boulders-v2" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // move the boulder through the portal and get the player position
        DungeonResponse d = world.tick(null, Direction.RIGHT);
        List<EntityResponse> entities = d.getEntities();

        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // try to move right into portal (should not be able to move)
        d = world.tick(null, Direction.RIGHT);
        entities = d.getEntities();
        
        // get positions after tick
        Position playerPos2 = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos2 = er.getPosition();
                break;
            }
        }

        // check player can't move
        assert(playerPos2.equals(playerPos));

    }

    /**
     * Test that if the other side of the twin portal is blocked, the player can't push a boulder through
     * MAP:
     * player B P _ _ P B B
     */
    @Test
    public void twinTeleportBlockBoulder() {
        // Create a new world
        World world = new World("portals-three-boulders", "Standard", 1);
        try {
            String file = FileLoader.loadResourceFile("/dungeons/" + "portals-three-boulders" + ".json");
            JSONObject game = new JSONObject(file);
            world.buildWorld(game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        

        // get initial player position
        DungeonResponse d = world.worldDungeonResponse();
        List<EntityResponse> entities = d.getEntities();

        Position playerPos = null;
        for (EntityResponse er : entities) {
            if (er.getType().equals("player")) {
                playerPos = er.getPosition();
                break;
            }
        }
        assertNotNull(playerPos);

        // try to move the boulder (should get stuck)
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

        // check the player couldn't move
        assert(playerPos2.equals(playerPos));

    }

}
