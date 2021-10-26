package dungeonmania.goal;

public interface ObserverTreasureGoal {

    // increase treasure collected count
    public void update(SubjectTreasureGoal treasure);



    // public ObserveTreasureGoal(int totalTreasure) {
    //     this.totalTreasure = totalTreasure;
    //     treasureCollected = 0;
    // }

    // @Override
    // public boolean isAchieved() {
    //     return treasureCollected == totalTreasure;
    // }

    // private void incrementTreasureCollected(){
    //     treasureCollected+=1;
    // }

    // @Override
    // public void update() {
    //     incrementTreasureCollected();
        
    // }
}
