package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.World;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean isTriggered;

    /**
     * Constructor for floor switch
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    public FloorSwitch(int x, int y, String id) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "switch");
        isTriggered = false;
    }

     /**
     * When a boulder is pushed onto a floor switch, it is triggered.
     * Also check for adjacent bombs
     */
    public void trigger(World world) {
        isTriggered = true;

        // check if there are any bombs
        List<Position> cardinallyAdj = this.getPosition().getCardinallyAdjacentPositions();

        List<StaticEntity> adjEntities = new ArrayList<>();
        for (Position pos : cardinallyAdj) {
            adjEntities.addAll(world.getStaticEntitiesAtPosition(pos));
        }

        for (StaticEntity e : adjEntities) {
            if (e instanceof PlacedBomb) {
                ((PlacedBomb) e).detonate(world);
            }
        }
        
    }

    /**
     * Pushing a boulder off the floor switch untriggers it.
     */
    public void untrigger() {
        isTriggered = false;
    }


}
