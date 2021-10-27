package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.goal.ObserverBoulderSwitchGoal;
import dungeonmania.goal.SubjectBoulderSwitchGoal;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements SubjectBoulderSwitchGoal {
    private boolean isTriggered;
    private ObserverBoulderSwitchGoal observer;

    public FloorSwitch(int x, int y, String id) {
        super(new Position(x, y, 0), id, "switch");
        isTriggered = false;
    }

    /**
     * Switches behave like empty squares, so other entities can appear on 
     * top of them. 
     */
    @Override
    public Position interact(World world, Entity entity) {
        return this.getPosition();
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
        
        notifyObservers();
    }

    /**
     * Pushing a boulder off the floor switch untriggers it.
     */
    public void untrigger() {
        isTriggered = false;
        notifyObservers();
    }

    @Override
    public void attach(ObserverBoulderSwitchGoal observer) {
        this.observer = observer;    
    }

    @Override
    public void notifyObservers() {
        observer.update(this);        
    }

    @Override
    public boolean isTriggered() {
        return isTriggered;
    }

   
}
