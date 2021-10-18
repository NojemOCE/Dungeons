package dungeonmania.character;

import dungeonmania.util.Position;

public class Battle {
    public Player player;
    public Character character;
    public Position position;

    public Battle(Player player, Character character) {
        this.player = player;
        this.character = character;
    }

    public void battleTick() {

    }

    public void endBattle() {

    }
}
