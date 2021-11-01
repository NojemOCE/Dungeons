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
    

    public AndGoal(String operator) {
        this.operator = operator;
        completed = false;
    }

    @Override
    public Boolean evaluate(World world) {
        for (GoalComponent g : subGoals) {
            g.evaluate(world);
        }

        //To be honest I have no idea why the above for loop needs to occur first, but it won't pass the tests without it
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
