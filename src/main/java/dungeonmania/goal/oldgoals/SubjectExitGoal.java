package dungeonmania.goal.oldgoals;

public interface SubjectExitGoal {

    public void attach(ObserverExitGoal observer);
	public void notifyObserver();
    
	// public void detach(ObserverExitGoal o);
    // whther exit has been reached
	// public boolean exitReached();


    // Boolean exitReached;

    // /*public ExitGoal () {
    //     exitReached = false;
    // }*/

    // @Override
    // public boolean isAchieved() {
    //     // if exitReached && all other goals achieved
    //     return false;
    // }

    // @Override
    // public void update() {
    //     exitReached = true;
    // }


    
}
