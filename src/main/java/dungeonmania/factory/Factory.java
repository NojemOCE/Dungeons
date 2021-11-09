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

    /**
     * Constructor for Factory taking in a GameMode
     * @param gamemode gamemode of factory
     */
    public Factory(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    /**
     * Increments and returns the next entity coutn
     * @return new entity count
     */
    protected int incrementEntityCount() {
        entityCount +=1;
        return entityCount;
    }

    /**
     * Sets the entity count at a given int
     * @param entityCount entity count to set
     */
    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    /**
     * Creates and returns an entity from a given JSON object, taking in the world
     * @param jsonObject JSON oject to create entity from
     * @param world world that the entity will be built into
     * @return entity
     */
    abstract public Entity createEntity(JSONObject jsonObject, World world);

    /**
     * Creates and returns an entity from x,y coordinates, string type, and current world
     * @param x x coordinate of the entity
     * @param y y coordinate of the entity
     * @param type string type of the entity
     * @param world world that the entity will be built into
     * @return entity
     */
    public Entity createEntity(int x, int y, String type, World world) {
        JSONObject newEntityObj = new JSONObject();
        newEntityObj.put("x", x);
        newEntityObj.put("y", y);
        newEntityObj.put("type", type);

        return createEntity(newEntityObj, world);
    }

    /**
     * Gets the total entity count
     * @return current entity count
     */
    public int getEntityCount() {
        return entityCount;
    }
    
    /**
     * Creates and returns a GoalComponent, taking in a JSON object
     * @param goal JSON object to build goal from
     * @return GoalComponent
     */
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
