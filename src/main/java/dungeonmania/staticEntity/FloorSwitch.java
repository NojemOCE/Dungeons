package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.World;
import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements Logic {
    private boolean isTriggered;
    private LogicComponent logic;


    /**
     * Constructor for floor switch
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    // public FloorSwitch(int x, int y, String id) {
    //     super(new Position(x, y, Position.FLOOR_LAYER), id, "switch");
    //     isTriggered = false;
    //     logic = null;
    // }

    /**
     * Constructor for floor switch with logic component
     * @param x x coordinate of exit
     * @param y y coordinate of exit
     * @param id id of exit
     */
    public FloorSwitch(int x, int y, String id, LogicComponent logic) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "switch");
        isTriggered = false;
        this.logic = logic;
    }

    /**
     * When a boulder is pushed onto a floor switch, it is triggered.
     * Also check for adjacent bombs
     */
    public void trigger(World world) {
        isTriggered = true;

        // check if there are any bombs
        // List<Position> cardinallyAdj = this.getPosition().getCardinallyAdjacentPositions();

        // List<StaticEntity> adjEntities = new ArrayList<>();
        // for (Position pos : cardinallyAdj) {
        //     adjEntities.addAll(world.getStaticEntitiesAtPosition(pos));
        // }

        // for (StaticEntity e : adjEntities) {
        //     if (e instanceof PlacedBomb) {
        //         ((PlacedBomb) e).detonate(world);
        //     }
        // }

        logic.notifyObservers(true);
        
    }

    /**
     * Pushing a boulder off the floor switch untriggers it.
     */
    public void untrigger() {
        isTriggered = false;
        logic.notifyObservers(false);
    }

    @Override
    public LogicComponent getLogic() {
        return logic;
    }


}
