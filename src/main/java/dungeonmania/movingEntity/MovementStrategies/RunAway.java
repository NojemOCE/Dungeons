package dungeonmania.movingEntity.MovementStrategies;

import dungeonmania.World;
import dungeonmania.movingEntity.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunAway implements Movement {

    @Override
    public void move(MovingEntity me, World world) {
       runAway(me, world);
    }

    /**
     * When the player is invincible, this moves the entity such that they are running away from the player
     * @param me moving entity to move
     * @param world current game world
     */
    private void runAway(MovingEntity me, World world) {

        Position player = world.getPlayer().getPosition();
        Position p = me.getPosition();
        int xOffset = p.getX() - player.getX();
        int yOffset = p.getY() - player.getY();

        

        if (xOffset > 0) {
            me.setPosition(me.validMove(p.translateBy(Direction.RIGHT), world));
        } else if (xOffset <0) {
            me.setPosition(me.validMove(p.translateBy(Direction.LEFT), world));
        } else { // the x is aligned, shift y instead
            if (yOffset > 0) {
                me.setPosition(me.validMove(p.translateBy(Direction.UP), world));
            } else if (yOffset < 0) {
                me.setPosition(me.validMove(p.translateBy(Direction.DOWN), world));

            }
        }
        
    }
    @Override
    public String getMovementType() {
        return "runAway";
    }
}
