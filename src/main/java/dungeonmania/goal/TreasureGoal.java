package dungeonmania.goal;

public class TreasureGoal extends Goal {
    private int treasureCollected;
    private int totalTreasure;

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
