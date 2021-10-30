package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;
import dungeonmania.movingEntity.*;
import dungeonmania.response.models.EntityResponse;

public class ZombieToastSpawn extends StaticEntity {

    /**
     * Constructor for ZombieToastSpawner
     * @param x x coordinate of the ZombieToastSpawn
     * @param y y coordinate of the ZombieToastSpawn
     * @param id id of the ZombieToastSpawn
     */
    public ZombieToastSpawn(int x, int y, String id) {
        super(new Position(x, y, 1), id, "zombie_toast_spawner");
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
    public void interact(World world) throws InvalidActionException {
        Player player = world.getPlayer();

        if (Position.isAdjacent(player.getPosition(), this.getPosition())) {
            if (world.playerHasWeapon()) {
                // destroy spawner
                List<Entity> thisSpawner = new ArrayList<>();
                thisSpawner.add(this);
                world.removeEntities(getPosition(), thisSpawner);
                return;
            } else {
                throw new InvalidActionException("Must have a weapon to destroy the spawner!");
            }
        } else {
            throw new InvalidActionException("Must be cardinally adjacent to the spawner to destroy it!");
        }
    }

    /**
     * Gets possible spawn positions (cells cardinally adjacent to the spawner).
     * @return returns list of possible spawn positions
     */
    public List<Position> spawn(){
        return getPosition().getCardinallyAdjacentPositions();
    }

    
    @Override
    public EntityResponse getEntityResponse() {
        return new EntityResponse(getId(), "zombie_toast_spawner", getPosition(), true);
    }
}
