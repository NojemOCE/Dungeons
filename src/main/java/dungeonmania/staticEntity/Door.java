package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.collectable.Key;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private String keyColour;
    private boolean isOpen = false;

    public Door(int x, int y, String id, String keyColour) {
        super(new Position(x, y, 1), id, "door");
        this.keyColour = keyColour;
    }

    private void open() {
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
    public Position interact(World world, MovingEntity character) {

        if (character instanceof Player) {
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
        
        return character.getPosition();
    }
    
    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "door", getPosition(), false);
    }
    
}
