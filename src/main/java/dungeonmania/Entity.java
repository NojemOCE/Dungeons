package dungeonmania;

import org.json.JSONObject;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity {

    private Position position;
    private String id;
    private String type;
    
    /**
     * Constructor for entity taking a position, an idea, and a type
     * @param position position of the entity
     * @param id unique ID of the entity
     * @param type string type of the entity
     */
    public Entity(Position position, String id, String type) {
        this.position = position;
        this.id = id;
        this.type = type;
    }

    /**
     * Get entity position
     * @return position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Gets the x coordinate of the entity
     * @return int for x coordinate of entity
     */
    public int getX() {
        return position.getX();
    }

    /**
     * Gets the y coordinate of the entity
     * @return int for y coordinate of entity
     */
    public int getY() {
        return position.getY();
    }

    /**
     * Gets the layer of the entity's position
     * @return int for entity's layer
     */
    public int getLayer() {
        return position.getLayer();
    }

    /**
     * Sets the entity's position at a given position
     * @param position position to set the entity at
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the unique entity id string
     * @return string of entity id
     */
    public String getId() {
        return id;
    }
  
    /**
     * Gets the entity type as a string
     * @return string of entity type
     */
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Gets an EntityResponse for an Entity
     * @return EntityResponse of Entity
     */
    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, type, getPosition(), false);
    }

    /**
     * Creates a JSON containing entity state required to save game
     * @return JSON object containing important variables for each entity so that when the game is re-loaded, it will load exactly as before
     */
    public abstract JSONObject saveGameJson();
}
