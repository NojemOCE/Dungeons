package dungeonmania.staticEntity;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity {
    private boolean activated = false;

    public LightBulb(int x, int y, String id) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "light_bulb");
    }
    
    @Override
    public EntityResponse getEntityResponse() {
        String status = "off";
        if (activated) {
            status = "on";
        }
        return new EntityResponse(getId(), getType() + "_" + status, getPosition(), false);
    }
}
