package dungeonmania.staticEntity;

import dungeonmania.util.Position;
import dungeonmania.character.Zombie;

public class ZombieToastSpawn extends StaticEntity {
    // spawn rate?

    public ZombieToastSpawn(Position position) {
        super(position);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void interact() {
        // TODO Auto-generated method stub
        
    }

    public Zombie spawn(){
        // should this return a zombie in the same position as the spawner
        return null;
    }
    
}
