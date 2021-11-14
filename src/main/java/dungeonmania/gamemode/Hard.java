package dungeonmania.gamemode;

public class Hard extends Gamemode {
    private final static int HARD_SPAWN_RATE = 15;
    private final static double HARD_STARTING_HP = 16; 

    /**
     * Constructor for hard gamemode
     */
    public Hard() {
        super(HARD_SPAWN_RATE, true, false, HARD_STARTING_HP, "hard");
    }
}
