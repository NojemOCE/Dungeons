package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.Spider;
import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    /**
     * Constructor for wall
     * @param x x coordinate of the wall
     * @param y y coordinate of the wall
     * @param id id of the wall
     */
    public Wall(int x, int y, String id) {
        super(new Position(x, y, Position.STATIC_LAYER), id, "wall");
    }

    /**
     * - Players cannot walk through walls
     * - Zombies and mercenaries are limited by the same constraints
     * - Spider can traverse through walls
     * 
     * Since characters are unable to walk through walls, they will end up in the same spot
     */
    @Override
    public Position interact(World world, Entity entity) {
        if (entity instanceof Spider) {
            return new Position(getX(), getY(), entity.getLayer());
        }
        return entity.getPosition();
    }

}
