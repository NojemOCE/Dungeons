package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Position;

abstract public class StaticEntity extends Entity {

    /**
     * Constructor for static entities
     * @param position position of the entity
     * @param id id of the entity
     * @param type the type of the entity
     */
    public StaticEntity(Position position, String id, String type) {
        super(position, id, type);
    }

    /**
     * Checks how the given entity interacts with this entity.
     * @return the position the given entity should move to
     */
    abstract public Position interact(World world, Entity entity);

}
