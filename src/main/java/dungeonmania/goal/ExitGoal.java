package dungeonmania.goal;

import java.util.List;

import dungeonmania.World;
import dungeonmania.staticEntity.Exit;
import dungeonmania.staticEntity.StaticEntity;

public class ExitGoal extends GoalLeaf {


    public ExitGoal(String goal) {
        super(goal);
    }

    @Override
    public Boolean evaluate(World world) {
        // TODO Auto-generated method stub
        List<StaticEntity> exitConfirmation = world.getStaticEntitiesAtPosition(world.getPlayer().getPosition());
        for (StaticEntity e : exitConfirmation) {
            if (e instanceof Exit) {
                setCompleted(true);
                return true;
            }
        }
        
        setCompleted(false);
        return false;
    }
    
}
