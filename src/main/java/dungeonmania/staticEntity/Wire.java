package dungeonmania.staticEntity;

import dungeonmania.util.Position;

public class Wire extends StaticEntity{
    private boolean activated = false;

    public Wire(int x, int y, String id) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "wire");
    }
    
}
