package dungeonmania.staticEntity;


import org.json.JSONObject;

import dungeonmania.World;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements Logic {
    private boolean isTriggered;
    private LogicComponent logic;

    /**
     * Constructor for floor switch with logic component
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    public FloorSwitch(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "switch");
        this.isTriggered = false;
        this.logic = logic;
    }

    /**
     * When a boulder is pushed onto a floor switch, it is triggered.
     * Also check for adjacent bombs
     * @param world current world
     */
    public void trigger(World world) {
        this.isTriggered = true;
        logic.notifyObservers(true);
        
    }

    /**
     * Pushing a boulder off the floor switch untriggers it.
     */
    public void untrigger() {
        isTriggered = false;
        logic.notifyObservers(false);
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
