package dungeonmania.character;

import dungeonmania.util.*;


public class Spider extends Character {

    public Spider(HealthPoint healthPoint, double attackDamage, Position position) {
        super(healthPoint, attackDamage, position);
        setAlly(false);

    }

    @Override
    public void move(Position position) {
        return;
    }

    @Override
    public void validMove(Position position) {
        // TODO Auto-generated method stub
        
    }
}
