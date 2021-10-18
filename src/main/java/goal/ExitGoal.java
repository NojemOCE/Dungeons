package goal;

public class ExitGoal extends Goal{

    
    // Exit exit; do we need to store this? or can we just update exitReached if the exit has been reached on that tick and all other goals are achieved
    // Boolean exitReached

    /*public ExitGoal (Exit exit) {
        this.exit = exit;
        exitReached = false;
    }*/

    @Override
    public boolean isAchieved() {
        // if exitReached && all other goals achieved
        return false;
    }

    
}
