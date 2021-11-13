package dungeonmania.staticEntity;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements Logic {
    private LogicComponent logic;

    /**
     * Constructor for light bulb
     * @param x x coordinate of light bulb
     * @param y y coordinate of light bulb
     * @param id id of light bulb
     * @param logic logic of the light bulb
     */
    public LightBulb(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "light_bulb");
        this.logic = logic;
    }

    /**
     * Acts like a wall - nothing can walk through it
     */
    @Override
    public Position interact(World world, Entity entity) {
        return entity.getPosition();
    }

    @Override
    public EntityResponse getEntityResponse() {
        String status = "off";
        if (isActivated()) {
            status = "on";
        }
        return new EntityResponse(getId(), getType() + "_" + status, getPosition(), false);
    }

    @Override
    public LogicComponent getLogic() {
        return logic;
    }
    
    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("logic", logic.logicString());
		return save;
	}
}
