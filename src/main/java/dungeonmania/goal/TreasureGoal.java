package goal;

public class TreasureGoal extends Goal {
    int treasureCollected;
    int totalTreasure;

    public TreasureGoal (int totalTreasure) {
        this.totalTreasure = totalTreasure;
        treasureCollected = 0;
    }

    @Override
    public boolean isAchieved() {
        return treasureCollected == totalTreasure;
    }

    private void incrementTreasureCollected(){
        treasureCollected+=1;
    }

    @Override
    public void update() {
        incrementTreasureCollected();
        
    }
}
