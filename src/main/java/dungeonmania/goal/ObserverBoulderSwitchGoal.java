package dungeonmania.goal;

public interface ObserverBoulderSwitchGoal {

    public void update(SubjectBoulderSwitchGoal obj);

    // int boulderSwitches;
    // int totalSwitches;

    // public ObserverBoulderSwitchGoal (int switches) {
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
