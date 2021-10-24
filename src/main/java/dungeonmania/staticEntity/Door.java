package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.collectable.Key;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private String keyColour;
    private boolean isOpen = false;

    public Door(int x, int y, String id, String keyColour) {
        super(new Position(x, y), id, "door");
        this.keyColour = keyColour;
    }

    private void open() {
        isOpen = true;
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
            if (world.inInventory(keyColour)) {
                // open door
                open();
                return this.getPosition();
            }
        }
        
        return character.getPosition();
    }
    
}
