package dungeonmania.goal;

public interface SubjectDefeatEnemiesGoal {

    public void attach(ObserverTreasureGoal o);
    // maybe not needed?
	public void detach(ObserverTreasureGoal o);
	public void notifyObservers();

    // whether an enemy is defeated
	public boolean isDefeated();

    // public 

    // int enemiesDefeated;
    // int totalEnemies;

    // public DefeatEnemiesGoal (int enemies) {
    //     this.totalEnemies = enemies;
    //     enemiesDefeated = 0;
    // }

    // @Override
    // public boolean isAchieved() {
    //     return enemiesDefeated == totalEnemies;
    // }

    // public void incrementTotalEnemies(){
    //     totalEnemies+=1;
    // }
    // public void incrementEnemiesDefeated(){
    //     enemiesDefeated+=1;
    // }

    // @Override
    // public void update() {
    //     // TODO Auto-generated method stub
        
    // }
}
