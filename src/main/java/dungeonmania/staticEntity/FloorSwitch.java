package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.goal.ObserverBoulderSwitchGoal;
import dungeonmania.goal.SubjectBoulderSwitchGoal;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements SubjectBoulderSwitchGoal {
    private boolean isTriggered;
    private ObserverBoulderSwitchGoal observer;

    public FloorSwitch(Position position) {
        super(position);
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

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "switch", getPosition(), false);
    }
    
}
