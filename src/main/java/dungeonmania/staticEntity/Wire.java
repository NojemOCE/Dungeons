package dungeonmania.staticEntity;

import dungeonmania.logic.Logic;
import dungeonmania.logic.LogicComponent;
import dungeonmania.logic.LogicLeaf;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements Logic {
    private boolean activated = false;
    private LogicComponent logic = new LogicLeaf();

    public Wire(int x, int y, String id) {
        super(new Position(x, y, Position.FLOOR_LAYER), id, "wire");
    }
    
    public LogicComponent getLogic() {
        return logic;
    }

    public void addObserver(LogicComponent e) {
        logic.addObserver(e);
    }

    public void removeObserver(LogicComponent e) {
        logic.removeObserver(e);
    }
}
