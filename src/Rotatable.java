import javafx.beans.property.DoubleProperty;

/**
 * An interface for 2D shape objects that can be rotated.
 */
public interface Rotatable {

    /**
     * Rotates the object by a given an point (x,y).
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    void rotate(double x, double y);

    /**
     * Returns the property holding X coordinate of object's rotation pivot.
     * 
     * @return object's rotationPivotX field
     */
    DoubleProperty rotationPivotXProperty();

    /**
     * Returns the property holding Y coordinate of object's rotation pivot.
     * 
     * @return object's rotationPivotY field
     */
    DoubleProperty rotationPivotYProperty();

}
