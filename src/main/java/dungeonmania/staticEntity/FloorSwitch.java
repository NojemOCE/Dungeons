package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {

    /**
     * Constructor for floor switch
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    public FloorSwitch(int x, int y, String id) {
        super(new Position(x, y, 0), id, "switch");
    }

    // public FloorSwitch(int x, int y, String id, boolean isTriggered) {
    //     super(new Position(x, y, 0), id, "switch");
    //     this.isTriggered = isTriggered;
    // }
    /**
     * Switches behave like empty squares, so other entities can appear on 
     * top of them. 
     */
    @Override
    public Position interact(World world, Entity entity) {
        return new Position(getX(), getY(), entity.getLayer());
    }

}
