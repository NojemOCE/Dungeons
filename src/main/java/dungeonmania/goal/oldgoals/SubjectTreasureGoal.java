package dungeonmania.goal.oldgoals;

public interface SubjectTreasureGoal {

    public void attach(ObserverTreasureGoal o);
	public void detach(ObserverTreasureGoal o);
	public void notifyObservers();

	public boolean isCollected();


    // int treasureCollected;
    // int totalTreasure;

    // public SubjectTreasureGoal (int totalTreasure) {
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
