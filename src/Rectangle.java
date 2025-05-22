import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

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
public class Rectangle extends javafx.scene.shape.Rectangle implements Movable, Resizable, Rotatable, Previewable, Repr {
    /** The initial pivot X coordinate, used as a reference point for previewing. */
    private final double pivotX;

    /** The initial pivot Y coordinate, used as a reference point for previewing. */
    private final double pivotY;

    private final DoubleProperty rotationPivotX = new SimpleDoubleProperty();
    private final DoubleProperty rotationPivotY = new SimpleDoubleProperty();

    /**
     * Constructs a new {@code Rectangle} with a pivot point at the specified coordinates.
     * The initial width and height are set to 0.
     *
     * @param x the X coordinate of the pivot point and rectangle origin
     * @param y the Y coordinate of the pivot point and rectangle origin
     */
    public Rectangle(double x, double y) {
        super(0.0, 0.0, 0.0, 0.0);
        pivotX = x;
        pivotY = y;
        setTranslateX(x);
        setTranslateY(y);
        rotationPivotX.bind(translateXProperty().add(widthProperty().divide(2)));
        rotationPivotY.bind(translateYProperty().add(heightProperty().divide(2)));
    }

    /**
     * Moves the rectangle such that its center is positioned at the specified coordinates.
     *
     * @param x the new X coordinate for the rectangle's center
     * @param y the new Y coordinate for the rectangle's center
     */
    @Override
    public void move(double x, double y) {
        setTranslateX(x - getWidth()/2);
        setTranslateY(y - getHeight()/2);
    }

    /**
     * Resizes the rectangle by scaling both width and height proportionally.
     * The rectangle grows or shrinks while maintaining its center.
     *
     * @param amount the scaling factor; positive to increase size, negative to decrease
     */
    @Override
    public void resize(double amount) {
        double scale = amount > 0 ? 1.06 : 0.98;
        setScaleX(this.getScaleX() * scale);
        setScaleY(this.getScaleY() * scale);
    }

    /**
     * Rotates the rectangle by calculating an angle based given point (x,y).
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @Override
    public void rotate(double x, double y) {
        setRotate(Math.toDegrees(-Math.atan(x/y)));
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
        setTranslateX(Math.min(pivotX, x));
        setTranslateY(Math.min(pivotY, y));
        setWidth(Math.abs(x - Math.max(getTranslateX(), pivotX)));
        setHeight(Math.abs(y - Math.max(getTranslateY(), pivotY)));
    }

    /**
     * Returns the property holding rectangle's rotation pivot X coordinate.
     * 
     * @return rectangle's rotationPivotX property
     */
    @Override
    public DoubleProperty rotationPivotXProperty() {
        return rotationPivotX;
    }

    /**
     * Returns the property holding rectangle's rotation pivot Y coordinate.
     * 
     * @return rectangle's rotationPivotY property
     */
    @Override
    public DoubleProperty rotationPivotYProperty() {
        return rotationPivotY;
    }

    /**
     * Constructs a serialization-ready representation of the rectangle.
     * 
     * @return Serialization-ready representation of the rectangle
     */
    @Override
    public Utils.ShapeRepr createRepr() {
        Utils.ShapeRepr repr = new Utils.ShapeRepr();
        repr.shapeType = Rectangle.class;
        repr.color = ((Color) getFill()).toString();
        repr.x = getTranslateX();
        repr.y = getTranslateY();
        repr.angle = getRotate();
        repr.scale = getScaleX();
        repr.args = new Double[]{getWidth(), getHeight()};

        return repr;
    }

    /**
     * Rectangle scpecific recreation step.
     * 
     * @param args array of shape specific parameters
     */
    @Override
    public void recreate(Double[] args) {
        setWidth(args[0]);
        setHeight(args[1]);
    }
}
