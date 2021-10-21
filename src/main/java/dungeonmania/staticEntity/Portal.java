package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {

    private Portal twinPortal;

    public Portal(Position position) {
        super(position);
    }

    public Portal(Position position, Portal twinPortal) {
        super(position);
        this.twinPortal = twinPortal;
        twinPortal.setTwinPortal(this);
    }

    @Override
    public Position interact(World world, MovingEntity character) {
        // TODO Auto-generated method stub
        return null;
    }

    public Portal getTwinPortal() {
        return twinPortal;
    }

    public void setTwinPortal(Portal twinPortal) {
        this.twinPortal = twinPortal;
    }
    
    
}
