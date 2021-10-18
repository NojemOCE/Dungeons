package dungeonmania.staticEntity;

import dungeonmania.collectable.Key;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private Key key;

    public Door(Position position, Key key) {
        super(position);
        this.key = key;
    }

    @Override
    public void interact() {
        // TODO Auto-generated method stub
        
    }
    
}
