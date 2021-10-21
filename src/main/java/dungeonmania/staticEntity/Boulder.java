package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class Boulder extends StaticEntity{

    
    public Boulder(Position position) {
        super(position);
    }

    @Override
    public Position interact(World world, MovingEntity character) {
        // TODO Auto-generated method stub
        return null;
    }
    public void move(Direction d){}
}
