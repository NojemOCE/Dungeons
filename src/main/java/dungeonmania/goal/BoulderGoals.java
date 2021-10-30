package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;


public class BoulderGoals extends GoalLeaf {

    public BoulderGoals(String goal) {
        super(goal);
    }

    @Override
    public Boolean evaluate(World world) {
        List<StaticEntity> boulders = new ArrayList<>(world.getStaticEntities().values());
        boulders.removeIf(obj -> !obj.getType().equals("boulder"));
        List<Position> boulderPositions = boulders.stream().map(b -> b.getPosition()).collect(Collectors.toList());
        
        List<StaticEntity> switches = new ArrayList<>(world.getStaticEntities().values());
        switches.removeIf(obj -> !obj.getType().equals("switch"));
        List<Position> switchPositions = switches.stream().map(s -> s.getPosition()).collect(Collectors.toList());
        if (boulderPositions.containsAll(switchPositions) && switchPositions.containsAll(boulderPositions)){
            setCompleted(true);
            return true;
        }
        else {
            setCompleted(false);
            return false;
        }
    }
    
}
