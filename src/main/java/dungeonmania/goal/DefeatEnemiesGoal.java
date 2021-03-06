package dungeonmania.goal;

public class DefeatEnemiesGoal extends Goal {
    private int enemiesDefeated;
    private int totalEnemies;

    public DefeatEnemiesGoal (int enemies) {
        this.totalEnemies = enemies;
        enemiesDefeated = 0;
    }

    @Override
    public boolean isAchieved() {
        return enemiesDefeated == totalEnemies;
    }

    public void incrementTotalEnemies(){
        totalEnemies+=1;
    }
    public void incrementEnemiesDefeated(){
        enemiesDefeated+=1;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }
}
