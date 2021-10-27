package dungeonmania.goal.oldgoals;

public interface ObserverDefeatEnemiesGoal {
    // increment number of enemies defeated
    public void update(SubjectDefeatEnemiesGoal obj);

    // increments num enemies in the world
    public void update();
    
    // int enemiesDefeated;
    // int totalEnemies;

    // public ObserverDefeatEnemiesGoal (int enemies) {
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
