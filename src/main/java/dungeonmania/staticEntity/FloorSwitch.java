package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean isCovered;

    public FloorSwitch(Position position) {
        super(position);
        isCovered = false;
    }

    @Override
    public Position interact(World world, MovingEntity character) {
        // TODO Auto-generated method stub
        return null;
    }

    public void cover() {}
    public boolean covered() {return false;}
    
}
