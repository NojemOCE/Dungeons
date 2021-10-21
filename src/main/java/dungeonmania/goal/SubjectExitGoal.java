package dungeonmania.goal;

public interface SubjectExitGoal {

    public void attach(ObserverTreasureGoal o);
	public void detach(ObserverTreasureGoal o);
	public void notifyObservers();

    // whther exit has been reached
	public boolean exitReached();


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
