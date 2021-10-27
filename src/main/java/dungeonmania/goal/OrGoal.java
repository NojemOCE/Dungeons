package dungeonmania.goal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrGoal implements GoalComponent{
    private String operator;
    private List<GoalComponent> subGoals = new ArrayList<>();
    private Boolean completed;
    
    public OrGoal(String operator) {
        this.operator = operator;
        completed = false;
    }

    @Override
    public Boolean evaluate() {
        // TODO Auto-generated method stub
        if (subGoals.stream().anyMatch(x -> x.evaluate().equals(true))) {
            completed = true;
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
                                            .collect(Collectors.joining(" "  + operator + " "));


        return unachievedSubGoals;
    }

    public void addSubGoal(GoalComponent subGoal) {
        subGoals.add(subGoal);
    }
}
