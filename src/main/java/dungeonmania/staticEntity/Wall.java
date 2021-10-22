package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    public Wall(Position position) {
        super(position);
    }

    /**
     * - Players cannot walk through walls
     * - Zombies and mercenaries are limited by the same constraints
     * - Spider can traverse through walls
     * 
     * Since characters are unable to walk through walls, they will end up in the same spot
     */
    // TODO: spider is ignored for now?? Since it implements its own movement
    @Override
    public Position interact(World world, MovingEntity character) {
        return character.getPosition();
    }

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "wall", getPosition(), false);
    }
    
}
