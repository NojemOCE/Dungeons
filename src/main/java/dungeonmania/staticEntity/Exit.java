package dungeonmania.staticEntity;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.goal.ObserverExitGoal;
import dungeonmania.goal.SubjectExitGoal;
import dungeonmania.movingEntity.Player;
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
    public Position interact(World world, Entity entity) {
        
        if (entity instanceof Player) {
            notifyObserver();
            return this.getPosition();
        }

        return entity.getPosition();
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

}
