package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.util.Position;
import dungeonmania.movingEntity.*;

public class ZombieToastSpawn extends StaticEntity {
    // spawn rate?

    public ZombieToastSpawn(Position position) {
        super(position);
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
    public Position interact(World world, MovingEntity character) {
        // TODO: is this called by controller??
        // interact != walk into in this case
        
        return character.getPosition();
    }


    public void interact(World world) {
        Player player = world.getPlayer();

        if (Position.isAdjacent(player.getPosition(), this.getPosition())) {
            // TODO: need to change this according to inventory
            // if (player.hasWeapon()) {
            //     // destroy spawner
            // }
        }
    }

    public Zombie spawn(){
        // should this return a zombie in the same position as the spawner
        // ^ doing this for now

        // Need HP and attack values?
        // Zombie newZombie = new Zombie(healthPoint, attackDamage, this.getPosition());

        return null;
    }
    
}
