import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

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
public class Polygon extends javafx.scene.shape.Polygon implements Movable, Resizable, Rotatable, Previewable, Repr {

    private final DoubleProperty rotationPivotX = new SimpleDoubleProperty();
    private final DoubleProperty rotationPivotY = new SimpleDoubleProperty();

    /**
     * Constructs a new {@code Polygon} with doubled points at specified coordinates.
     * First point acts as the starting point, the second one is used for preview functionality.
     *
     * @param x the X coordinate of the polygon origin
     * @param y the Y coordinate of the polygon origin
     */
    public Polygon(double x, double y) {
        super(0.0, 0.0, 0.0, 0.0);
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Moves the polygon such that it's centroid is at point (x,y).
     *
     * @param x the new X coordinate for the rectangle's center
     * @param y the new Y coordinate for the rectangle's center
     */
    @Override
    public void move(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Resizes the polygon by scaling it by given factor.
     *
     * @param amount the scaling factor; positive to increase size, negative to decrease
     */
    @Override
    public void resize(double amount) {
        double scale = amount > 0 ? 1.05 : 0.95;
        setScaleX(this.getScaleX() * scale);
        setScaleY(this.getScaleY() * scale);
    }

    /**
     * Rotates the polygon by calculating an angle based given point (x,y).
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @Override
    public void rotate(double x, double y) {
        if (y < 0) {
            this.setRotate(Math.toDegrees(-Math.atan(x/y)));
        } else {
            this.setRotate(Math.toDegrees(-Math.atan(x/y)) + 180);
        }
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
        this.getPoints().set(this.getPoints().size()-2, x-getTranslateX());
        this.getPoints().set(this.getPoints().size()-1, y-getTranslateY());
    }

    /**
     * Determines whether given point is within threshold distance of the polygon's origin.
     * 
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return {@code true} if the point is near the starting point (within 10 units), {@code false} otherwise
     */
    public Polygon nextPoint(double x, double y) {
        if (Utils.distance(x, y, getTranslateX(), getTranslateY()) < 10) {
            getPoints().removeLast();
            getPoints().removeLast();
            updateCentroid();
            return null;
        } else {
            getPoints().addAll(x, y);
            return this;
        }
    }

    /**
     * Returns the property holding polygon's rotation pivot X coordinate.
     * 
     * @return polygon's rotationPivotX property
     */
    @Override
    public DoubleProperty rotationPivotXProperty() {
        return rotationPivotX;
    }

    /**
     * Returns the property holding polygon's rotation pivot Y coordinate.
     * 
     * @return polygon's rotationPivotY property
     */
    @Override
    public DoubleProperty rotationPivotYProperty() {
        return rotationPivotY;
    }

    /**
     * Updates the centroid (geometric center) of the shape based on its point list,
     * and re-binds the rotation pivot properties to follow the shape's translation
     * offset plus the calculated centroid.
     */
    private void updateCentroid() {
        double x = 0;
        double y = 0;
        for (int i = 0; i < getPoints().size(); i += 2) {
            x += getPoints().get(i);
            y += getPoints().get(i+1);
        }
        rotationPivotX.bind(translateXProperty().add(x/getPoints().size()*2));
        rotationPivotY.bind(translateYProperty().add(y/getPoints().size()*2));
    }

    /**
     * Constructs a serialization-ready representation of the polygon.
     * 
     * @return Serialization-ready representation of the polygon
     */
    @Override
    public Utils.ShapeRepr createRepr() {
        Utils.ShapeRepr repr = new Utils.ShapeRepr();
        repr.shapeType = Polygon.class;
        repr.color = ((Color) getFill()).toString();
        repr.x = getTranslateX();
        repr.y = getTranslateY();
        repr.angle = getRotate();
        repr.scale = getScaleX();
        repr.args = new Double[getPoints().size()+2];
        repr.args[0] = rotationPivotX.get() - translateXProperty().get();
        repr.args[1] = rotationPivotY.get() - translateYProperty().get();
        for (int i=2; i<repr.args.length; i++) {
            repr.args[i] = getPoints().get(i-2);
        }

        return repr;
    }

    /**
     * Polygon scpecific recreation step.
     * 
     * @param args array of shape specific parameters
     */
    @Override
    public void recreate(Double[] args) {
        rotationPivotX.bind(translateXProperty().add(args[0]));
        rotationPivotY.bind(translateYProperty().add(args[1]));
        for (int i = 2; i < args.length; i++) {
            getPoints().add(args[i]); 
        }
    }
}
