package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.goal.ObserverBoulderSwitchGoal;
import dungeonmania.goal.SubjectBoulderSwitchGoal;
import dungeonmania.movingEntity.MovingEntity;
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
    public Position interact(World world, MovingEntity character) {
        return this.getPosition();
    }

    /**
     * When a boulder is pushed onto a floor switch, it is triggered.
     */
    public void trigger() {
        isTriggered = true;
    }

    /**
     * Pushing a boulder off the floor switch untriggers it.
     */
    public void untrigger() {
        isTriggered = false;
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
