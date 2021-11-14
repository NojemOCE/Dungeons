package dungeonmania.goal;
import org.json.JSONObject;

import dungeonmania.World;


public interface GoalComponent {

    /**
     * Evaluates the goal
     * @param world current world
     * @return true if the goal is achieved, false otherwise
     */
    public Boolean evaluate(World world);

    /**
     * Gets remaining goal string
     * @return remaining goal string
     */
    public String remainingGoalString();

    /**
     * Gets the JSONObject to write the goal component to the save game file
     * @return JSONObbject containing all essential goal details
     */
    public abstract JSONObject saveGameJson();
}
