package dungeonmania.collectable;

import dungeonmania.Consumable;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.Door;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Consumable {
    private String keyColour;
    
    public Key(int x, int y, String id, String keyColour) {
        super(new Position(x, y, 1), id, "invisibility_potion");
        this.keyColour = keyColour;
    }

    // Can we remove this variable?
    private Door door;



    public void consume() {};

    public void craft() {};

    public void unlock() {};

    public String getKeyColour() {
        return keyColour;
    }

}
