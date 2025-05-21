import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;

/**
 * A custom {@link javafx.scene.shape.Polygon} that supports dynamic transformations
 * such as moving, resizing, rotating, and previewing. This class is designed for
 * interactive graphical applications where user-defined manipulations of shapes are needed.
 *
 * <p>This class implements the following interfaces:</p>
 * <ul>
 *   <li>{@code Movable} - for repositioning the rectangle based on a center point.</li>
 *   <li>{@code Resizable} - for proportionally resizing the rectangle.</li>
 *   <li>{@code Rotatable} - for applying rotation to the rectangle.</li>
 *   <li>{@code Previewable} - for dynamically adjusting rectangle bounds based on user input.</li>
 * </ul>
 */
public class Polygon extends javafx.scene.shape.Polygon implements Movable, Resizable, Rotatable, Previewable {

    /** Shape center's X coordinate, used as a reference point for rotating */
    private final DoubleProperty centerX = new SimpleDoubleProperty(0);

    /** Shape center's Y coordinate, used as a reference point for rotating */
    private final DoubleProperty centerY = new SimpleDoubleProperty(0);

    /**
     * Constructs a new {@code Polygon} with doubled points at specified coordinates.
     * First point acts as the starting point, the second one is used for preview functionality.
     *
     * @param x the X coordinate of the polygon origin
     * @param y the Y coordinate of the polygon origin
     */
    public Polygon(double x, double y) {
        super(x, y, x, y);
    }

    /**
     * Moves the polygon such that //TODO
     *
     * @param x the new X coordinate for the rectangle's center
     * @param y the new Y coordinate for the rectangle's center
     */
    @Override
    public void move(double x, double y) {
        ObservableList<Double> points = this.getPoints();
        double deltaX = x - this.centerX.get();
        double deltaY = y - this.centerY.get();
        centerX.set(centerX.get() + deltaX);
        centerY.set(centerY.get() + deltaY);
        for (int i=0; i<points.size(); i=i+2) {
            points.set(i, points.get(i)+deltaX);
            points.set(i+1, points.get(i+1)+deltaY);
        }
    }

    /**
     * Resizes the polygon by scaling both width and height proportionally.
     * The rectangle grows or shrinks while maintaining its center.//TODO
     *
     * @param amount the scaling factor; positive to increase size, negative to decrease
     */
    @Override
    public void resize(double ammount) {
        //TODO: polygon resize
    }

    /**
     * Rotates the polygon by adding a scaled rotation amount to its current angle.
     *
     * @param angle the amount to rotate (scaled internally by 0.1)
     */
    @Override
    public void rotate(double x, double y) {
        this.setRotate(Math.toDegrees(-Math.atan((centerX.get()-x)/(centerY.get()-y))));
    }

    /**
     * Updates the polygons's last point to preview a bounding box
     * comprised of previously set points and previewed coordinates.
     *
     * @param x the X coordinate of the previewed point
     * @param y the Y coordinate of the previewed point
     */
    @Override
    public void preview(double x, double y) {
        this.getPoints().set(this.getPoints().size()-2, x);
        this.getPoints().set(this.getPoints().size()-1, y);
    }

    /**
     * Determines whether given point is within threshold distance of the polygon's origin.
     * 
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return {@code true} if the point is near the starting point (within 10 units), {@code false} otherwise
     */
    public boolean isNearStartPoint(double x, double y) {
        return Utils.distance(x, y, this.getPoints().get(0), this.getPoints().get(1)) < 10;
    }

    public void finish() {
        this.getPoints().removeLast();
        this.getPoints().removeLast();
        for (int i=0; i<this.getPoints().size(); i=i+2) {
            centerX.set(centerX.get() + this.getPoints().get(i));
            centerY.set(centerY.get() + this.getPoints().get(i+1));
        }
        centerX.set(centerX.get()/this.getPoints().size()*2);
        centerY.set(centerY.get()/this.getPoints().size()*2);

    }

    @Override
    public DoubleProperty centerXProperty() {
        return centerX;
    }

    @Override
    public DoubleProperty centerYProperty() {
        return centerY;
    }
    
}
