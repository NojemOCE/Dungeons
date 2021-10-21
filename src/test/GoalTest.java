public class GoalTest {

    //I am wondering if we should have the following observer realtionships:
    // World observes entities that impact goals ie.
        // World implements ObserverXXXGoal
        // Relevant entities implement SubjectXXXGoal
    // Goals observe world
        // Goals implement ObserverXXXGoal
        // World implements SubjectXXXGoal
    // But I don't know if this is actually allowed


    /**Test Enemy goal notes
     * Create Entity/World (whichever is instance of Subject)
     * Create enemyGoal (instance of observer)
     * 
     * world.attach(enemyGoal)
     * 
     * If an enemy dies, they will need to be removed from the world (?),
     * BUT first, the goal should be updated to be notified of an enemies death
     * so world will notify goal to increase the number of enemies defeated
     * 
     * Need to think about how to incorporate the number of enemies increasing
     * 
     */

     /**Test boulder/switch goal notes
     * Create Entity/World (whichever is instance of Subject)
     * Create boulderSwitchGoal (instance of observer)
     * 
     * world.attach(boulderSwitchGoal)
     * 
     * If a switch is covered by the boulder, the world should notify the relevant goal
     * Goal should then increase no. of switches covered
     * 
     */

    /**Test exit goal notes
     * Create Entity/World (whichever is instance of Subject)
     * Create exitGoal (instance of observer)
     * 
     * world.attach(exitGoal)
     * 
     * If a player uses the exit, the world should be notified(maybe)
     * IFF all other goals are met, world notifies exitGoal to be updated
     * this will also end the game
     */

    /**Test Treasure goal
     * Create Entity/World (whichever is instance of Subject)
     * Create treasureGoal (instance of observer)
     * 
     * world.attach(treasureGoal)
     * If the player collects a piece of treasure, the world will know
     * The world should then notify the goal to increase the no. of treasure collected
     */
}
