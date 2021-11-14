package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.World;


public class AndGoal implements GoalComponent {
    private String operator;
    private List<GoalComponent> subGoals = new ArrayList<>();
    private Boolean completed;
    

    /**
     * Construtor for and goal taking an operator
     * @param operator should always be "and"
     */
    public AndGoal(String operator) {
        this.operator = operator;
        completed = false;
    }

    @Override
    public Boolean evaluate(World world) {
        for (GoalComponent g : subGoals) {
            g.evaluate(world);
        }

        if (subGoals.stream().allMatch(x -> x.evaluate(world).equals(true))) {
            completed = true;
        }
        else {
            completed = false;
        }
        return completed;
        
    }

    @Override
    public String remainingGoalString() {
        if (completed) {
            return "";
        }
        String unachievedSubGoals = subGoals.stream()
                                            .map(GoalComponent :: remainingGoalString)
                                            .filter(x -> !x.equals(""))
                                            .collect(Collectors.joining(" "  + operator + " "));

        if (unachievedSubGoals.contains(operator)) {
            return "(" + unachievedSubGoals + ")";
        }
        else return unachievedSubGoals;
        
    }
    
    /**
     * Adds a subgoal to the and goal
     * @param subGoal subgoal to add
     */
    public void addSubGoal(GoalComponent subGoal) {
        subGoals.add(subGoal);
    }

    @Override
    public JSONObject saveGameJson() {
        JSONObject compositeGoalJSON = new JSONObject();
        compositeGoalJSON.put("goal", operator);

        JSONArray subGoalsJSON = new JSONArray();
        subGoals.stream().map(GoalComponent :: saveGameJson).forEach(x -> subGoalsJSON.put(x));
        
        compositeGoalJSON.put("subgoals", subGoalsJSON);
        return compositeGoalJSON;
    }

}
