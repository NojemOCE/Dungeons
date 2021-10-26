package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;

abstract public class StaticEntity extends Entity {
    // Do we need a string field for the unique identifier?
    private Position position;
    

    public StaticEntity(Position position, String id, String type) {
        super(position, id, type);
    }


    abstract public Position interact(World world, MovingEntity character);


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
