package dungeonmania.staticEntity;

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
    public void interact() {
        // TODO Auto-generated method stub
        
    }

    public Portal getTwinPortal() {
        return twinPortal;
    }

    public void setTwinPortal(Portal twinPortal) {
        this.twinPortal = twinPortal;
    }
    
    
}
