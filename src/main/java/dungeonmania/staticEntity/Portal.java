package dungeonmania.staticEntity;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Zombie;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {

    private Portal twinPortal;
    private String colour;

    /**
     * Constructor for first portal in pair
     * @param x x coordinates of position
     * @param y y coordinates of position
     * @param id id of the entity
     * @param colour colour of the portal
     */
    public Portal(int x, int y, String id, String colour) {
        super(new Position(x, y, 1), id, "portal");
        this.colour = colour;
    }

    /**
     * Constructor for second portal in pair
     * @param x x coordinates of position
     * @param y y coordinates of position
     * @param id id of the entity
     * @param colour colour of the portal
     * @param twinPortal portal that pairs with this portal
     */
    public Portal(int x, int y, String id, String colour, Portal twinPortal) {
        super(new Position(x, y, 1), id, "portal");
        this.twinPortal = twinPortal;
        this.colour = colour;
        twinPortal.setTwinPortal(this);
    }

    /**
     * Teleports entities to a corresponding portal.
     * All entities but zombies are able to moce through portals
     */
    @Override
    public Position interact(World world, Entity entity) {
        if (!(entity instanceof Zombie)) {
            travelToTwin(world, entity);
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
    private Position travelToTwin(World world, Entity entity) {
        // we need to end up on the opposite side of the portal
        int entityX = entity.getPosition().getX();
        int entityY = entity.getPosition().getY();
        int portalX = this.getPosition().getX();
        int portalY = this.getPosition().getY();
    
        Position toMoveTo;
        if (entityY == portalY) {
            // if left of
            if (entityX < portalX) {
                toMoveTo = twinPortal.getPosition().translateBy(Direction.RIGHT);
            } else {
                // is right of
                toMoveTo = twinPortal.getPosition().translateBy(Direction.LEFT);
            }
        } else {
            // if above
            if (entityY < portalY) {
                toMoveTo = twinPortal.getPosition().translateBy(Direction.DOWN);
            } else {
                // is below
                toMoveTo = twinPortal.getPosition().translateBy(Direction.UP);
            }
        }
        
        // check that toMoveTo is a valid position
        if (entity instanceof MovingEntity) {
            return ((MovingEntity) entity).validMove(toMoveTo, world);
        } else if (entity instanceof Boulder) {
            // if it is not a moving entity, it can only be a boulder
            if (((Boulder) entity).validMove(world, toMoveTo)) {
                return toMoveTo;
            }
        }

        return entity.getPosition();
    }

    /**
     * Getter for the pair portal for this portal
     * @return
     */
    public Portal getTwinPortal() {
        return twinPortal;
    }

    /**
     * Set given portal as the pair for this portal
     * @param twinPortal portal to set as twin portal
     */
    public void setTwinPortal(Portal twinPortal) {
        this.twinPortal = twinPortal;
    }

    /**
     * Geter for the colour of the portal
     * @return the colour of the portal
     */
    public String getColour() {
        return colour;
    }

    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("colour", colour);
		return save;
	}
}
