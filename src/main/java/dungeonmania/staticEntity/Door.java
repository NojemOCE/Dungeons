package dungeonmania.staticEntity;

import java.util.Objects;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.collectable.Key;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.Spider;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyColour;
    private boolean isOpen = false;

    /**
     * Constructor for door
     * @param x x coordinate of the door
     * @param y y coordinate of the door
     * @param id id of the door
     * @param keyColour unique key pair
     */
    public Door(int x, int y, String id, int keyColour) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "door");
        this.keyColour = keyColour;
    }

    /**
     * Constructor for door
     * @param x x coordinate of the door
     * @param y y coordinate of the door
     * @param id id of the door
     * @param keyColour unique key pair
     * @param opened boolean for whether the door is opened
     */
    public Door(int x, int y, String id, int keyColour, boolean opened) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "door");
        this.keyColour = keyColour;
        this.isOpen = opened;
    }

    /**
     * Opens the door
     */
    public void open() {
        isOpen = true;
    }
    
    /**
     * Getter for the key "colour" (ie. code) of this door
     * @return the key colour of this door
     */
    public int getKeyColour() {
        return keyColour;
    }
    /**
     * - Players can only walk through doors if they hold the matching key
     *      - If they have the key, the door will open and they will move onto the door
     *      - Else, they stay in the same spot
     * - Spiders are able to walk through doors
     * - All other characters cannot walk through doors at any point
     */
    @Override
    public Position interact(World world, Entity entity) {

        if (entity instanceof Player) {
            if (isOpen) {
                return new Position(getX(), getY(), entity.getLayer());
            }
            Key key = world.keyInInventory(keyColour);
            if (world.inInventory("sun_stone")) {
                open();
                return new Position(getX(), getY(), entity.getLayer());
            } else if (!Objects.isNull(key)) {
                // open door
                open();
                // use the key
                world.use(key.getId());
                return new Position(getX(), getY(), entity.getLayer());
            } 
        } else if (entity instanceof Spider) {
            return new Position(getX(), getY(), entity.getLayer());
        }
        
        return entity.getPosition();
    }
   

    @Override
    public EntityResponse getEntityResponse() {
        if (isOpen) {
            return new EntityResponse(getId(), "door_open", getPosition(), false);
        } else if (keyColour % 2 == 0) {
            return new EntityResponse(getId(), "door2", getPosition(), false);
        } else {
            return super.getEntityResponse();
        }
    }
    
    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("key", keyColour);
        save.put("open", isOpen);
		return save;
	}
}
