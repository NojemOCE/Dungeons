package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.Zombie;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {

    private Portal twinPortal;
    // private String colour;

    public Portal(int x, int y, String id) {
        super(new Position(x, y, 1), id, "portal");

    }

    public Portal(int x, int y, String id, Portal twinPortal) {
        super(new Position(x, y, 1), id, "portal");
        this.twinPortal = twinPortal;
        twinPortal.setTwinPortal(this);
    }

    /**
     * Teleports entities to a corresponding portal.
     * All entities but zombies are able to moce through portals
     */
    @Override
    public Position interact(World world, Entity entity) {
        if (!(entity instanceof Zombie)) {
            travelToTwin(entity);
        }   
        // otherwise they just step on this spot
        return this.getPosition();
    }
    
    
    /**
     * Returns the location which the character should move to 
     * after travelling through the portal
     * @param character the character to move
     * @return the position to move the character to
     */
    private Position travelToTwin(Entity character) {
        // we need to end up on the opposite side of the portal
        int charX = character.getPosition().getX();
        int charY = character.getPosition().getY();
        int portalX = this.getPosition().getX();
        int portalY = this.getPosition().getY();
    
        if (charY == portalY) {
            // if left of
            if (charX < portalX) {
                return twinPortal.getPosition().translateBy(Direction.RIGHT);
            } else {
                // is right of
                return twinPortal.getPosition().translateBy(Direction.LEFT);
            }
        } else {
            // if above
            if (charY < portalY) {
                return twinPortal.getPosition().translateBy(Direction.DOWN);
            } else {
                // is below
                return twinPortal.getPosition().translateBy(Direction.UP);
            }
        }
        
    }

    public Portal getTwinPortal() {
        return twinPortal;
    }

    public void setTwinPortal(Portal twinPortal) {
        this.twinPortal = twinPortal;
    }

}
