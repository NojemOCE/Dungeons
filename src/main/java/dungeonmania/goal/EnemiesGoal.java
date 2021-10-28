package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.World;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.StaticEntity;


public class EnemiesGoal extends GoalLeaf {


    public EnemiesGoal(String goal) {
        super(goal);
    }

    @Override
    public Boolean evaluate(World world) {
        List<MovingEntity> list = new ArrayList<>(world.getMovingEntities().values());
        List<StaticEntity> zts = new ArrayList<>(world.getStaticEntities().values());
        zts.removeIf((obj -> !obj.getType().equals("zombie_toast_spawner")));

        if (list.isEmpty() && zts.isEmpty()) return true;
        else return false;
    }
    
}
