package dungeonmania.staticEntity;

import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements Logic {
    private LogicComponent logic;

    public LightBulb(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "light_bulb");
        this.logic = logic;
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
}
