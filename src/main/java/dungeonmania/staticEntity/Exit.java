package dungeonmania.staticEntity;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    /**
     * Constructor for Exit
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    public Exit(int x, int y, String id) {
        super(new Position(x, y, 0), id, "exit");

    }

}
