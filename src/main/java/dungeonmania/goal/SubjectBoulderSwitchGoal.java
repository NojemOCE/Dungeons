package dungeonmania.goal;

public interface SubjectBoulderSwitchGoal {

    public void attach(ObserverTreasureGoal o);
	public void detach(ObserverTreasureGoal o);
	public void notifyObservers();

    // whether a switch has a boulder on top of it
    // true if activated
    // false if it is removed
	public boolean isActivated();

    // int boulderSwitches;
    // int totalSwitches;

    // public BoulderSwitchGoal (int switches) {
    //     this.totalSwitches = switches;
    //     boulderSwitches = 0;
    // }

    // @Override
    // public boolean isAchieved() {
    //     return boulderSwitches == totalSwitches;
    // }

    // private void incrementBoulderSwitches(){
    //     boulderSwitches+=1;
    // }

    // @Override
    // public void update() {
    //     incrementBoulderSwitches();
        
    // }
}
