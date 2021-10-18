package dungeonmania.staticEntity;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean isCovered;

    public FloorSwitch(Position position) {
        super(position);
        isCovered = false;
    }

    @Override
    public void interact() {
        // TODO Auto-generated method stub
        
    }

    
}
