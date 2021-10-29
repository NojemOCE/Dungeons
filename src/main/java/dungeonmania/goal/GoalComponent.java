package dungeonmania.goal;
import org.json.JSONObject;

import dungeonmania.World;


public interface GoalComponent {
    public Boolean evaluate(World world); // will this take in world?

    public String remainingGoalString();
    public abstract JSONObject saveGameJson();
}
