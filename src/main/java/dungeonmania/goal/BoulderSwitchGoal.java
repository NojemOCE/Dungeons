package dungeonmania.goal;

public class BoulderSwitchGoal extends Goal {
    private int boulderSwitches;
    private int totalSwitches;

    public BoulderSwitchGoal (int switches) {
        this.totalSwitches = switches;
        boulderSwitches = 0;
    }

    @Override
    public boolean isAchieved() {
        return boulderSwitches == totalSwitches;
    }

    private void incrementBoulderSwitches(){
        boulderSwitches+=1;
    }

    @Override
    public void update() {
        incrementBoulderSwitches();
        
    }
}
