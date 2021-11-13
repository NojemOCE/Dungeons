package dungeonmania.staticEntity;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.Spider;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity implements Logic {
    private LogicComponent logic;

    /**
     * Constructor for door
     * @param x x coordinate of the door
     * @param y y coordinate of the door
     * @param id id of the door
     * @param logic logic of the door
     */
    public SwitchDoor(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "switch_door");
        this.logic = logic;
    }

    /**
     * - Players can only walk through doors if it is open
     * - Spiders are able to walk through doors
     * - All other characters cannot walk through doors at any point
     */
    @Override
    public Position interact(World world, Entity entity) {

        if (entity instanceof Player) {
            if (isActivated()) {
                return new Position(getX(), getY(), entity.getLayer());
            }
            // TODO: to check if we want this 
            // if (world.inInventory("sun_stone")) {
            //     open();
            //     return new Position(getX(), getY(), entity.getLayer());
            // }
        } else if (entity instanceof Spider) {
            return new Position(getX(), getY(), entity.getLayer());
        }
        
        return entity.getPosition();
    }
   

    @Override
    public EntityResponse getEntityResponse() {
        if (isActivated()) {
            return new EntityResponse(getId(), "switch_door_open", getPosition(), false);
        } else {
            return super.getEntityResponse();
        }
    }
    
    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("logic", logic.logicString());
        save.put("open", isActivated());
		return save;
	}


    @Override
    public LogicComponent getLogic() {
        return logic;
    }
}
