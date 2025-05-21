import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * A custom {@link javafx.scene.shape.Rectangle} that supports dynamic transformations
 * such as moving, resizing, rotating, and previewing. This class is designed for
 * interactive graphical applications where user-defined manipulations of shapes are needed.
 *
 * <p>The rectangle maintains an internal pivot point which serves as a reference
 * for operations such as previewing (e.g., drawing a rectangle dynamically on drag).</p>
 *
 * <p>This class implements the following interfaces:</p>
 * <ul>
 *   <li>{@code Movable} - for repositioning the rectangle based on a center point.</li>
 *   <li>{@code Resizable} - for proportionally resizing the rectangle.</li>
 *   <li>{@code Rotatable} - for applying rotation to the rectangle.</li>
 *   <li>{@code Previewable} - for dynamically adjusting rectangle bounds based on user input.</li>
 * </ul>
 */
public class Rectangle extends javafx.scene.shape.Rectangle implements Movable, Resizable, Rotatable, Previewable {

    /** Shape center's X coordinate, used as a reference point for rotating */
    private final DoubleProperty centerX = new SimpleDoubleProperty();

    /** Shape center's Y coordinate, used as a reference point for rotating */
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    /** The initial pivot X coordinate, used as a reference point for previewing. */
    private final double pivotX;

    /** The initial pivot Y coordinate, used as a reference point for previewing. */
    private final double pivotY;

    /**
     * Constructs a new {@code Rectangle} with a pivot point at the specified coordinates.
     * The initial width and height are set to 0.
     *
     * @param x the X coordinate of the pivot point and rectangle origin
     * @param y the Y coordinate of the pivot point and rectangle origin
     */
    public Rectangle(double x, double y) {
        super(x, y, 0.0, 0.0);
        pivotX = x;
        pivotY = y;
    }

    /**
     * Moves the rectangle such that its center is positioned at the specified coordinates.
     *
     * @param x the new X coordinate for the rectangle's center
     * @param y the new Y coordinate for the rectangle's center
     */
    @Override
    public void move(double x, double y) {
        this.setX(x - this.getWidth()/2);
        this.setY(y - this.getHeight()/2);
        this.centerX.set(x);
        this.centerY.set(y);
    }

    /**
     * Resizes the rectangle by scaling both width and height proportionally.
     * The rectangle grows or shrinks while maintaining its center.
     *
     * @param amount the scaling factor; positive to increase size, negative to decrease
     */
    @Override
    public void resize(double ammount) {
        this.setWidth(this.getWidth() + ammount * 0.1);
        this.setHeight(this.getHeight() + ammount * 0.1);
        this.setX(this.getX() - ammount * 0.05);
        this.setY(this.getY() - ammount * 0.05);
    }

    /**
     * Rotates the rectangle by adding a scaled rotation amount to its current angle.
     *
     * @param angle the amount to rotate (scaled internally by 0.1)
     */
    @Override
    public void rotate(double x, double y) {
        this.setRotate(Math.toDegrees(-Math.atan((centerX.get()-x)/(centerY.get()-y))));
    }

    /**
     * Updates the rectangle's position and dimensions to preview a bounding box
     * from the pivot point to the given coordinates.
     *
     * @param x the X coordinate of the opposite corner from the pivot
     * @param y the Y coordinate of the opposite corner from the pivot
     */
    @Override
    public void preview(double x, double y) {
        this.setX(Math.min(this.pivotX, x));
        this.setY(Math.min(this.pivotY, y));
        this.setWidth(Math.abs(x - Math.max(this.getX(), this.pivotX)));
        this.setHeight(Math.abs(y - Math.max(this.getY(), this.pivotY)));
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
