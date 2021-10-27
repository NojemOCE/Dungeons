package dungeonmania.staticEntity;

import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.util.Position;
import dungeonmania.movingEntity.*;
import dungeonmania.response.models.EntityResponse;

public class ZombieToastSpawn extends StaticEntity {
    int spawnRate;

    public ZombieToastSpawn(int x, int y, String id, int spawnRate) {
        super(new Position(x, y, 1), id, "zombie_toast_spawner");
        this.spawnRate = spawnRate;
    }

    /**
     * Spawns zombie toasts every 20 ticks in an open square cardinally adjacent to the spawner.
     */

    /**
     *  
     * The character can destroy a zombie spawner if they have a 
     * weapon and are cardinally adjacent to the spawner.
     */
    @Override
    public Position interact(World world, Entity entity) {
        // TODO: is this called by controller??
        // interact != walk into in this case
        
        return entity.getPosition();
    }



    /**
     *  
     * The character can destroy a zombie spawner if they have a 
     * weapon and are cardinally adjacent to the spawner.
     */
    public void interact(World world) {
        Player player = world.getPlayer();

        if (Position.isAdjacent(player.getPosition(), this.getPosition())) {
            // TODO: need to change this according to inventory
            // if (player.hasWeapon()) {
            //     // destroy spawner
            // }
        }
    }

    /**
     * Spawns zombie toasts in an open square cardinally adjacent to the spawner.
     * @return
     */
    public Zombie spawn(Gamemode gameMode){
        // should this return a zombie in the same position as the spawner
        // ^ doing this for now
        Random random = new Random();
        List<Position> adjPositions = this.getPosition().getAdjacentPositions();

        // need world?

        // Zombie newZombie = new Zombie(, y, id, gameMode);

        return null;
    }

    
    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), "zombie_toast_spawner", getPosition(), true);
    }
}
