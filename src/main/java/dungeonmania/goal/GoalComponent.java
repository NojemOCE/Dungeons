package dungeonmania.goal;
import dungeonmania.World;


public interface GoalComponent {
    public Boolean evaluate(World world); // will this take in world?

    public String remainingGoalString();
}
