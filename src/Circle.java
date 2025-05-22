import javafx.scene.paint.Color;

/**
 * A custom {@link javafx.scene.shape.Circle} that supports dynamic transformations
 * such as moving, resizing, and previewing. This class is designed for
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
public class Circle extends javafx.scene.shape.Circle implements Movable, Resizable, Previewable, Repr {
    /**
     * Constructs a new {@code Circle} with center at specified coordinates.
     * The initial radius is set to 0.
     *
     * @param x the X coordinate of the circle origin
     * @param y the Y coordinate of the circle origin
     */
    public Circle(double x, double y) {
        super(0, 0, 0);
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Moves the circle's center.
     *
     * @param x the new X coordinate for the circle's center
     * @param y the new Y coordinate for the circle's center
     */
    @Override
    public void move(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Resizes the circle by scaling the radius proportionally.
     *
     * @param amount the scaling factor; positive to increase size, negative to decrease
     */
    @Override
    public void resize(double amount) {
        setRadius(getRadius() + amount * 0.1);
    }

    /**
     * Updates the circle's radius to preview a bounding box
     * from the origin to the given coordinates.
     *
     * @param x the X coordinate of circle's edge
     * @param y the Y coordinate of circle's edge
     */
    @Override
    public void preview(double x, double y) {
        setRadius(Utils.distance(getTranslateX(), getTranslateY(), x, y));
    }

    /**
     * Constructs a serialization-ready representation of the circle.
     * 
     * @return Serialization-ready representation of the circle
     */
    @Override
    public Utils.ShapeRepr createRepr() {
        Utils.ShapeRepr repr = new Utils.ShapeRepr();
        repr.shapeType = Circle.class;
        repr.color = String.format( "#%02X%02X%02X",
        (int)( ((Color) getFill()).getRed() * 255 ),
        (int)( ((Color) getFill()).getGreen() * 255 ),
        (int)( ((Color) getFill()).getBlue() * 255 ) );
        repr.x = getTranslateX();
        repr.y = getTranslateY();
        repr.angle = 0;
        repr.scale = 1.0;
        repr.args = new Double[]{getRadius()};

        return repr;
    }

    /**
     * Circle scpecific recreation step.
     * 
     * @param args array of shape specific parameters
     */
    @Override
    public void recreate(Double[] args) {
        setRadius(args[0]);
    }

}
