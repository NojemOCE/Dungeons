package dungeonmania.goal;

public class GoalLeaf implements GoalComponent {
    private String goal;
    private Boolean completed;

    
    public GoalLeaf(String goal) {
        this.goal = goal;
        completed = false;
    }

    @Override
    public Boolean evaluate() {
        // TODO Auto-generated method stub
        return completed;
    }

    @Override
    public String remainingGoalString() {
        if (completed) {
            return "";
        }

        return goal;
    }


    
}
