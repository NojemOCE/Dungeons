package dungeonmania.staticEntity;

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

}
