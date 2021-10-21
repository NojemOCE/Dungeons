package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.collectable.Key;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private Key key;

    public Door(Position position, Key key) {
        super(position);
        this.key = key;
    }

    @Override
    public Position interact(World world, MovingEntity character) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
