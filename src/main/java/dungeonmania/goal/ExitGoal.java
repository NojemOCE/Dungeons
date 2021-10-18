package dungeonmania.goal;

public class ExitGoal extends Goal{

    private Boolean exitReached;

    /*public ExitGoal () {
        exitReached = false;
    }*/

    @Override
    public boolean isAchieved() {
        // if exitReached && all other goals achieved
        return false;
    }

    @Override
    public void update() {
        exitReached = true;
    }


    
}
