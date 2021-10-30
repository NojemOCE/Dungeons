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
        List<MovingEntity> enemiesOnly = new ArrayList<>(world.getMovingEntities().values());
        enemiesOnly.removeIf(obj -> obj.getAlly());
        List<StaticEntity> zts = new ArrayList<>(world.getStaticEntities().values());
        zts.removeIf((obj -> !obj.getType().equals("zombie_toast_spawner")));

        if (enemiesOnly.isEmpty() && zts.isEmpty()) {
            setCompleted(true);
            return true;
        }
        else {
            setCompleted(false);
            return false;
        }
    }
    
}
