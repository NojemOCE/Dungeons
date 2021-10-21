package dungeonmania.character;

import dungeonmania.util.Position;

public class Battle {
    private Player player;
    private Character character;
    private Position position;

    public Battle(Player player, Character character) {
        this.player = player;
        this.character = character;
    }

    public void battleTick() {

    }

    public Player getPlayer() {
        return player;
    }

    public Character getCharacter() {
        return character;
    }

    public Position getPosition() {
        return position;
    }

    private void endBattle() {

    }
}
