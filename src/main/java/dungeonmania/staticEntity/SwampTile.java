package dungeonmania.staticEntity;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {

    public SwampTile(int x, int y, String id) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "swamp_tile");
    }
    
    
}
