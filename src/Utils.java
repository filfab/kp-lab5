import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Static class containing utility functions
 */
public class Utils {
    private Utils() {}

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param x0 x-coordinate of the first point
     * @param y0 y-coordinate of the first point
     * @param x1 x-coordinate of the second point
     * @param y1 y-coordinate of the second point
     * @return the distance between the points (x0, y0) and (x1, y1)
     */
    public static double distance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
    }

    public static Shape createShape(Class<?> clazz, double x, double y, Color color) {
        try {
            Shape shape = (Shape) clazz.getConstructor(double.class, double.class).newInstance(x, y);
            shape.setFill(color);
            return shape;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }

}