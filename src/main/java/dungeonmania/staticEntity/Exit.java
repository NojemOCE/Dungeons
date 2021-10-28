package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    public Exit(int x, int y, String id) {
        super(new Position(x, y), id, "exit");

    }

    /**
     * If the player goes through it, the puzzle is complete.
     * No effect otherwise.
     */
    @Override
    public Position interact(World world, Entity entity) {
        
        if (entity instanceof Player) {
            return this.getPosition();
        }

        return entity.getPosition();
    }

}
