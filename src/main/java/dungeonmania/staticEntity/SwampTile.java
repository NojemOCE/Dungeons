package dungeonmania.staticEntity;

import org.json.JSONObject;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;

    /**
     * Constructor for swamp tile
     * @param x x coordinate of swamp tile
     * @param y y coordinate of swamp tile
     * @param id id of the swamp tile
     * @param movementFactor how many ticks it takes enemies to traverse
     */
    public SwampTile(int x, int y, String id, int movementFactor) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "swamp_tile");
        this.movementFactor = movementFactor;
    }
    
    @Override
	public JSONObject saveGameJson() {
		JSONObject save = super.saveGameJson();
        save.put("movement_factor", movementFactor);
		return save;
	}

    /**
     * Getter for the movement factor of the tile
     * @return integer the movement factor
     */
    public int getMovementFactor() {
        return movementFactor;
    }
    
}
