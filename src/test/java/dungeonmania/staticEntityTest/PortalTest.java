package dungeonmania.staticEntityTest;


import dungeonmania.staticEntity.*;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


public class PortalTest {
    @Test
    public void constructionTest(){

        Position portalPos = new Position(1,1);
        Portal portal = new Portal(portalPos);
        assertNotNull(portal);
        assert(portalPos.equals(portal.getPosition()));
    
    }
    @Test
    public void assignTwinPortal(){

        Position portalPos = new Position(1,1);
        Portal portal = new Portal(portalPos);
        assertNull(portal.getTwinPortal());

        Position twinPortalPos = new Position(1,5);
        Portal twinPortal = new Portal(twinPortalPos, portal);

        assertNotNull(twinPortal);
        assert(twinPortalPos.equals(twinPortal.getPosition()));

        // Will we have to write an equals method for this?
        assertEquals(twinPortal, portal.getTwinPortal());
        assertEquals(portal, twinPortal.getTwinPortal());

    
    }

}
