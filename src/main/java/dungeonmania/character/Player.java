package dungeonmania.character;

import dungeonmania.util.*;


public class Player extends Character {

    private Inventory inventory;

    public Player(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(true);
    }


  
    @Override
    public void move(Position position) {
        return;
    }

    @Override
    public void validMove(Position position) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double attack() {
        // check inventory and mercenary in range

        return getAttackDamage();
    }

    @Override
    public double defend() {
        // check inventory and mercenary in range

        return 0;
    }

}  
