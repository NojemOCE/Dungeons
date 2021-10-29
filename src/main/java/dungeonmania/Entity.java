package dungeonmania;

import org.json.JSONObject;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity {

    private Position position;
    private String id;
    private String type;

    private boolean interactable;

    
    public Entity(Position position, String id, String type) {
        this.position = position;
        this.id = id;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }
  
    public String getType() {
        return type;
    }
    
    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, type, getPosition(), false);
    }

    /**
     * Creates a JSON containing entity state required to save/load game
     * @return JSON object containing important variables
     */
    public abstract JSONObject saveGameJson();
}
