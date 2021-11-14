package dungeonmania.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Position {
    private final int x, y, layer;

    public final static int MOVING_LAYER = 2;
    public final static int STATIC_LAYER = 1;
    public final static int FLOOR_LAYER = 0;


    public Position(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.layer = 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(x, y, layer);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;

        // z doesn't matter
        return x == other.x && y == other.y;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getLayer() {
        return layer;
    }

    public final Position asLayer(int layer) {
        return new Position(x, y, layer);
    }

    public final Position translateBy(int x, int y) {
        return this.translateBy(new Position(x, y));
    }

    public final Position translateBy(Direction direction) {
        return this.translateBy(direction.getOffset());
    }

    public final Position translateBy(Position position) {
        return new Position(this.x + position.x, this.y + position.y, this.layer + position.layer);
    }

    // (Note: doesn't include z)

    /**
     * Calculates the position vector of b relative to a (ie. the direction from a
     * to b)
     * @return The relative position vector
     */
    public static final Position calculatePositionBetween(Position a, Position b) {
        return new Position(b.x - a.x, b.y - a.y);
    }

    /**
     * Checks if two given positions are cardinally adjacent
     * @param a First position to check
     * @param b Second position to check
     * @return whether the positions are cardinally adjacent
     */
    public static final boolean isAdjacent(Position a, Position b) {
        int x = a.x - b.x;
        int y = a.y - b.y;
        return ((x + y == 1) || (x + y == - 1));
    }

    // (Note: doesn't include z)
    public final Position scale(int factor) {
        return new Position(x * factor, y * factor, layer);
    }

    @Override
    public final String toString() {
        return "Position [x=" + x + ", y=" + y + ", z=" + layer + "]";
    }

    // Return Adjacent positions in an array list with the following element positions:
    // 0 1 2
    // 7 p 3
    // 6 5 4
    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x-1, y-1));
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    /**
     * Returns all positions that are cardinally 2 tiles from the current position
     * @return a list of positions of all the 2 tile adjacent positions
     */
    public List<Position> get2AdjacentPosition() {
        List<Position> adjacentPositions = new ArrayList<>();

        adjacentPositions.add(new Position(x  , y-2));
        adjacentPositions.add(new Position(x+2, y));
        adjacentPositions.add(new Position(x  , y+2));
        adjacentPositions.add(new Position(x-2, y));
        
        return adjacentPositions;
    }


    // Return Cardinally Adjacent positions in an array list with the following element positions:
    //   0
    // 3 p 1
    //   2
    public List<Position> getCardinallyAdjacentPositions() {
        List<Position> adjacentPositions = getAdjacentPositions();
        List<Position> cardinalPositions = new ArrayList<>();
        for (int i = 1; i <= 7; i += 2) {
            cardinalPositions.add(adjacentPositions.get(i));
        }

        return cardinalPositions;
    }
}
