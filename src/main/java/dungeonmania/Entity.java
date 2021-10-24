package dungeonmania;

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

    protected void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }
  
    public String getType() {
        return type;
    }
    
}
