import javafx.beans.property.DoubleProperty;

/**
 * An interface for 2D shape objects that can be rotated.
 */
public interface Rotatable {

    /**
     * Rotates the object by a given angle or factor.
     * 
     * @param angle the amount to rotate (implementation may scale or interpret as degrees/radians)
     */
    void rotate(double x, double y);

    DoubleProperty centerXProperty();

    DoubleProperty centerYProperty();

}
