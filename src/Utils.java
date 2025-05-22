import java.io.Serializable;

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

    /**
     * Initializes given shape at point (x,y) and provided color.
     * 
     * @param clazz shape to be initialized
     * @param x     origin x coordinate
     * @param y     origin y coordinate
     * @param color shape's fill color
     * @return      {@code Shape} initialazed with given parameters, or {@code null} initialization was unsuccessfull
     */
    public static Shape createShape(Class<?> clazz, double x, double y, Color color) {
        try {
            Shape shape = (Shape) clazz.getConstructor(double.class, double.class).newInstance(x, y);
            shape.setFill(color);
            shape.setStrokeWidth(5);
            return shape;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }


    /**
     * A generic pointer-like wrapper class that holds a reference to a value of type {@code T}.
     *
     * @param <T> the type of the value being pointed to
     */
    public static class Pointer<T> {

        /**
         * The value under the pointer.
         */
        private T value;

        /**
         * Constructs a new {@code Pointer} with the given initial value.
         *
         * @param _value the initial value to point to
         */
        public Pointer(T _value) {
            value = _value;
        }

        /**
         * Sets the value being pointed to.
         *
         * @param _value the new value to point to
         */
        public void set(T _value) {
            value = _value;
        }

        /**
         * Returns the value currently being pointed to.
         *
         * @return the current value
         */
        public T value() {
            return value;
        }

        /**
         * Checks whether the pointer value is currently null.
         *
         * @return {@code true} if the value is {@code null}; {@code false} otherwise
         */
        public boolean isNull() {
            return value == null;
        }
    }

    /**
     * A serializable representation of a shape.
     */
    public static class ShapeRepr implements Serializable {

        /** Shape type */
        Class<? extends Shape> shapeType;
        /** Shape color */
        String color;
        /** Shape's X translation */
        double x;
        /** Shape's Y translation */
        double y;
        /** Shape's rotation angle */
        double angle;
        /** Shape's scale factor */
        double scale;
        /** Shape specific parameters */
        Double[] args;

        /**
         * Recreate a shape based on the representation.
         * 
         * @return recreated shape
         */
        public Shape recreate() {
            Shape shape = Utils.createShape(shapeType, x, y, Color.web(color));
            shape.setTranslateX(x);
            shape.setTranslateY(y);
            shape.setRotate(angle);
            shape.setScaleX(scale);
            shape.setScaleY(scale);
            ((Repr) shape).recreate(args);

            return shape;
        }
    }
}