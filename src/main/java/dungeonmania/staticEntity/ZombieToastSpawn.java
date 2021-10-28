package dungeonmania.staticEntity;

import java.util.ArrayList;
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
     * Zombie toast spawner acts like a wall unless interacted with through click
     */
    @Override
    public Position interact(World world, Entity entity) {      
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
            if (world.playerHasWeapon()) {
                // destroy spawner
                List<Entity> thisSpawner = new ArrayList<>();
                thisSpawner.add(this);
                world.removeEntities(getPosition(), thisSpawner);
            }
        }
    }

    /**
     * Spawns zombie toasts in an open square cardinally adjacent to the spawner.
     * @return
     */
    public Zombie spawn(String id, Gamemode gameMode){
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
