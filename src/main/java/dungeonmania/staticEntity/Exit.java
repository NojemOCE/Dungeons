package dungeonmania.staticEntity;

import dungeonmania.World;
import dungeonmania.goal.ObserverExitGoal;
import dungeonmania.goal.SubjectExitGoal;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Exit extends StaticEntity implements SubjectExitGoal {
    ObserverExitGoal observer;

    public Exit(int x, int y, String id) {
        super(new Position(x, y), id, "exit");

    }

    /**
     * If the player goes through it, the puzzle is complete.
     * No effect otherwise.
     */
    @Override
    public Position interact(World world, MovingEntity character) {
        
        if (character instanceof Player) {
            notifyObserver();
            return this.getPosition();
        }

        return character.getPosition();
    }


    // Goal methods:

    @Override
    public void attach(ObserverExitGoal observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObserver() {
        observer.update(this);
    }

    @Override
    public EntityResponse getEntityResponse() {
        // TODO Update for ID
        return new EntityResponse("not a real ID", "exit", getPosition(), false);
    }
}
