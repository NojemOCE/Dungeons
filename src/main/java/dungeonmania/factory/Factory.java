package dungeonmania.factory;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.World;
import dungeonmania.gamemode.Gamemode;
import dungeonmania.goal.*;

public abstract class Factory {
    Gamemode gamemode;
    int entityCount = 0;
    int highestX = 5;
    int highestY = 5;

    public Factory(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    protected int incrementEntityCount() {
        entityCount +=1;
        return entityCount;
    }

    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    abstract public Entity createEntity(JSONObject jsonObject, World world);

    public Entity createEntity(int x, int y, String type, World world) {
        JSONObject newEntityObj = new JSONObject();
        newEntityObj.put("x", x);
        newEntityObj.put("y", y);
        newEntityObj.put("type", type);
        updateBounds(x, y);

        return createEntity(newEntityObj, world);
    }

    public int getEntityCount() {
        return entityCount;
    }

    /**
     * Sets the largest bounds of the map
     * @param x x co-ordinate
     * @param y y co-ordinate
     */
    public void updateBounds(int x, int y) {
        if (x > highestX) {
            highestX = x;
        }
        if (y > highestY) {
            highestY = y;
        }
    }

    public int getHighestX() {
        return highestX;
    }

    public int getHighestY() {
        return highestY;
    }
    
    
    public GoalComponent createGoal(JSONObject goal){
        String currGoal = goal.getString("goal");

        // Will return null if the goal is not exit,enemies,treasure, AND or OR
        if (currGoal.equals("exit")) {
            return new ExitGoal(currGoal);
        }
        else if (currGoal.equals("enemies")) {
            return new EnemiesGoal(currGoal);
        }
        else if (currGoal.equals("boulders")) {
            return new BoulderGoals(currGoal);
        }
        else if (currGoal.equals("treasure")) {
            return new TreasureGoals(currGoal);
        }
        else if (currGoal.equals("AND")) {
            AndGoal andGoal = new AndGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                andGoal.addSubGoal(subGoal);
            }
            return andGoal; 
        }
        else if (currGoal.equals("OR")) {
            OrGoal orGoal = new OrGoal(currGoal);
            JSONArray subGoals = goal.getJSONArray("subgoals");

            for (int i = 0; i < subGoals.length(); i++) {
                GoalComponent subGoal = createGoal(subGoals.getJSONObject(i));
                orGoal.addSubGoal(subGoal);
            }
            return orGoal; 
        }

        return null;
    }
    
}
