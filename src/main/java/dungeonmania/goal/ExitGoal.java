package dungeonmania.goal;

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
        StaticEntity exitConfirmation = world.getStaticEntity(world.getPlayer().getPosition());
        if (exitConfirmation instanceof Exit) return true;
        else return false;
    }
    
}
