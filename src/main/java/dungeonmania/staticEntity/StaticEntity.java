package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

abstract public class StaticEntity {
    // Do we need a string field for the unique identifier?
    private Position position;
    

    public StaticEntity(Position position) {
        this.position = position;
    }


    abstract public Position interact(World world, MovingEntity character);


    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    abstract public EntityResponse getEntityResponse();
    
}
