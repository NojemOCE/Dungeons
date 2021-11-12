package dungeonmania.staticEntity;

import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.logic.OrLogic;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements Logic {
    // By operation, wires implement the "or" logic
    private LogicComponent logic = new OrLogic();

    public Wire(int x, int y, String id) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "wire");
    }
    
    public LogicComponent getLogic() {
        return logic;
    }
    
}
