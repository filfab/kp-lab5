/**
 * An interface for 2D shape objects that can be repositioned.
 */
public interface Movable {

    /**
     * Moves the object to a new location based on the given coordinates.
     *
     * @param x the new X coordinate (typically the center or anchor point)
     * @param y the new Y coordinate (typically the center or anchor point)
     */
    void move(double x, double y);
}
