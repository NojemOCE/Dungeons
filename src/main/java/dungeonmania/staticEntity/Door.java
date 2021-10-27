package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.collectable.Key;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private String keyColour;
    private boolean isOpen = false;

    public Door(int x, int y, String id, String keyColour) {
        super(new Position(x, y, 1), id, "door");
        this.keyColour = keyColour;
    }

    public void open() {
        isOpen = true;
    }
    
    public String getKeyColour() {
        return keyColour;
    }
    /**
     * - Players can only walk through doors if they hold the matching key
     *      - If they have the key, the door will open and they will move onto the door
     *      - Else, they stay in the same spot
     * - All other characters cannot walk through doors at any point
     */
    @Override
    public Position interact(World world, Entity entity) {

        if (entity instanceof Player) {
            if (isOpen) {
                return this.getPosition();
            }

            // TODO: check that there is a key with this key colour
            Key key = world.keyInInventory(keyColour);
            if (!key.equals(null)) {
                // open door
                open();
                // use the key
                world.use(key);
                return this.getPosition();
            }
        }
        
        return entity.getPosition();
    }
   
}
