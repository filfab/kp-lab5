/**
 * Enum representing different button types and their associated shape classes and labels.
 */
public enum Buttons {

    /** Button for creating a circle shape, associated with the Circle class. */
    CIRCLE(Circle.class, "circle"),

    /** Button for creating a rectangle shape, associated with the Rectangle class. */
    RECTANGLE(Rectangle.class, "rect"),

    /** Button for creating a polygon shape, associated with the Polygon class. */
    POLYGON(Polygon.class, "poly"),

    /** Button for editing mode; not associated with a shape class. */
    EDIT(Boolean.class, "edit");

    /** The shape class associated with this button (may be null). */
    private final Class<?> cls;

    /** The string label representing the button. */
    private final String text;

    /**
     * Constructor for the Buttons enum.
     *
     * @param _cls  the class representing the shape associated with the button (nullable)
     * @param _text the display text for the button
     */
    private Buttons(Class<?> _cls, String _text) {
        this.cls = _cls;
        this.text = _text;
    }

    /**
     * Returns the text label for this button.
     *
     * @return the string representation of the button
     */
    @Override
    public String toString() {
        return this.text;
    }

    /**
     * Returns the class associated with this button.
     *
     * @return the Class object for the shape, or null if none
     */
    public Class<?> shape() {
        return this.cls;
    }

    /**
     * Utility class to manage selection of a Buttons enum.
     */
    public static class ButtonSelector {

        /** The currently selected button (nullable). */
        private Buttons button;

        /**
         * Constructs an empty ButtonSelector with no selected button.
         */
        ButtonSelector() {
            button = null;
        }

        /**
         * Sets the current selected button.
         *
         * @param arg0 the button to select
         */
        public void set(Buttons arg0) {
            this.button = arg0;
        }

        /**
         * Gets the currently selected button.
         *
         * @return the selected Buttons enum, or null if none
         */
        public Buttons get() {
            return this.button;
        }

        /**
         * Returns the shape class associated with the selected button.
         *
         * @return the Class object for the shape, or null if no button is selected or has no class
         */
        public Class<?> getShape() {
            return this.button.cls;
        }
    }
}
