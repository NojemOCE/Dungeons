package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.World;

public abstract class GoalLeaf implements GoalComponent {
    private String goal;
    private Boolean completed;

    
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

        return goal;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getGoal() {
        return goal;
    }

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
