package dungeonmania.goal;

public interface GoalComponent {
    public Boolean evaluate(); // will this take in world?

    public String remainingGoalString();
}
