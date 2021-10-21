package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.util.Position;
import dungeonmania.movingEntity.*;

public class ZombieToastSpawn extends StaticEntity {
    // spawn rate?

    public ZombieToastSpawn(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Position interact(World world, MovingEntity character) {
        // TODO Auto-generated method stub
        return null;
    }

    public Zombie spawn(){
        // should this return a zombie in the same position as the spawner
        return null;
    }
    
}
