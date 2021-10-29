package dungeonmania.staticEntity;

import org.json.JSONObject;

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

    
	@Override
	public JSONObject saveGameJson() {
		JSONObject save = new JSONObject();
        save.put("x", getPosition().getX());
        save.put("y", getPosition().getY());
        save.put("id", getId());
        save.put("type", getType());
		return save;
	}
}
