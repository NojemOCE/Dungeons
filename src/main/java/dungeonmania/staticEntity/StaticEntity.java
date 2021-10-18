package dungeonmania.staticEntity;

import dungeonmania.util.Position;

abstract public class StaticEntity {
    // Do we need a string field for the unique identifier?
    Position position;
    

    public StaticEntity(Position position) {
        this.position = position;
    }


    abstract public void interact();


    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    
    
}
