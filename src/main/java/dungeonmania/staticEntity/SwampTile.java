package dungeonmania.staticEntity;

import org.json.JSONObject;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;
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
    
}
