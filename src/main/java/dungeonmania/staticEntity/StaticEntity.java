package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Position;

abstract public class StaticEntity extends Entity {

    public StaticEntity(Position position, String id, String type) {
        super(position, id, type);
    }


    abstract public Position interact(World world, Entity entity);

}
