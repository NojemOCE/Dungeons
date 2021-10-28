package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.World;
import dungeonmania.collectable.*;

public class TreasureGoals extends GoalLeaf{

    
    public TreasureGoals(String goal) {
        super(goal);
    }

    @Override
    public Boolean evaluate(World world) {
        // TODO Auto-generated method stub
        List<CollectableEntity> ce = new ArrayList<>(world.getCollectibleEntities().values());
        // check no more treasure
        ce.removeIf(obj -> !obj.getType().equals("treasure"));

        return ce.isEmpty();
    }
    
}
