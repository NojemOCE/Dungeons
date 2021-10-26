package dungeonmania.gamemode;

public class Hard extends Gamemode {
    private final static int HARD_SPAWN_RATE = 15;
    private final static double HARD_STARTING_HP = 10; // TODO

    public Hard() {
        super(HARD_SPAWN_RATE, true, false, HARD_STARTING_HP);
    }
}
