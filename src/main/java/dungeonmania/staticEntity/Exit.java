package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
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

    /**
     * If the player goes through it, the puzzle is complete.
     * No effect otherwise. All entities can stand on the exit.
     */
    @Override
    public Position interact(World world, Entity entity) {
        return new Position(getX(), getY(), entity.getLayer());
    }

}
