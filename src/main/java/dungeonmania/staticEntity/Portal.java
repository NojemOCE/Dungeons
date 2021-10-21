package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {

    private Portal twinPortal;

    public Portal(Position position) {
        super(position);
    }

    public Portal(Position position, Portal twinPortal) {
        super(position);
        this.twinPortal = twinPortal;
        twinPortal.setTwinPortal(this);
    }

    /**
     * Teleports entities to a corresponding portal.
     * It is assumed that both player and mecernary can travel through portals
     * Other enemies (zombie and spider) cannot, they just walk over them
     */
    @Override
    public Position interact(World world, MovingEntity character) {
        if (character instanceof Player || character instanceof Mercenary) {
            travelToTwin(character);
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
    private Position travelToTwin(MovingEntity character) {
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
