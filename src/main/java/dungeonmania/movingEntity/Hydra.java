package dungeonmania.movingEntity;

import java.util.Random;

import dungeonmania.movingEntity.States.State;

public class Hydra extends Zombie {
    static final int HYDRA_ATTACK = 4;
    static final int HYDRA_HEALTH = 25;

    /**
     * Constructor for hydra taking x and y coords, and unique ID
     * @param x x coordinate
     * @param y y coordinate
     * @param id unique ID of hydra
     */
    public Hydra(int x, int y, String id) {
        super(x, y, id);
        setHealthPoint(new HealthPoint(HYDRA_HEALTH));
        setAttackDamage(HYDRA_ATTACK);
        setType("hydra");
    }

    /**
     * Constructor for hydra taking x and y coords, and unique ID
     * @param x x coordinate
     * @param y y coordinate
     * @param id unique ID of hydra
     * @param defaultMovement default movement
     * @param currentMovement current movement
     * @param state movement state
     */
    public Hydra(int x, int y, String id, HealthPoint hp, MovementStrategy defaultMovement,
    MovementStrategy currentMovement, State state) {
        super(x, y, id, hp, defaultMovement, currentMovement, state);
        setAttackDamage(HYDRA_ATTACK);
        setType("hydra");
    }
    
    @Override
    public void defend(double attack) {
        if (attack < 0) {
            getHealthPoint().loseHealth(Math.abs(attack));
            return;
        }

        Random r = new Random();
        if (r.nextInt(100) < 50) {
            if (getHealthPoint().getHealth() + attack >= getHealthPoint().getMaxHealth()) {
                getHealthPoint().setMaxHealth(getHealthPoint().getHealth() + attack);
                return;
            }
            getHealthPoint().gainHealth(attack);

        } else {
            getHealthPoint().loseHealth(attack);
        }
    }
    
    
}
