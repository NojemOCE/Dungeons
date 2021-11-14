package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.World;

public abstract class GoalLeaf implements GoalComponent {
    private String goal;
    private Boolean completed;

    /**
     * Constructor for goal leaf taking a goal string
     * @param goal goal string
     */
    public GoalLeaf(String goal) {
        this.goal = goal;
        completed = false;
    }

    @Override
    public abstract Boolean evaluate(World world);

    @Override
    public String remainingGoalString() {
        if (completed) {
            return "";
        }

        return ":" + goal;
    }

    /**
     * Gets whether the goal has been completed
     * @return true if completed, false otherwise
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Sets the goals completed status to a given boolean
     * @param completed boolean to set completion status to
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    /**
     * Gets the goal string
     * @return goal string
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Sets the goal string to a given string
     * @param goal string to set goal to
     */
    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject goalJSON = new JSONObject();
        goalJSON.put("goal", goal);
        
        return goalJSON;
    }
    
}
