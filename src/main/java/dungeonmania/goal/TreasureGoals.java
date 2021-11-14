package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.World;
import dungeonmania.collectable.*;

public class TreasureGoals extends GoalLeaf{

    /**
     * Constructor for treasure goal taking a goal string
     * @param goal goal string, should be "treasure"
     */
    public TreasureGoals(String goal) {
        super(goal);
    }

    @Override
    public Boolean evaluate(World world) {
    
        List<CollectableEntity> ce = new ArrayList<>(world.getCollectibleEntities().values());
        ce.removeIf(obj -> !obj.getType().equals("treasure"));
        
        if (ce.isEmpty()){
            setCompleted(true);
            return true;
        }
        else {
            setCompleted(false);
            return false;
        }
    }
    
}
