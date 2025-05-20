public enum Buttons {
    CIRCLE(Circle.class, "circle"),
    RECTANGLE(Rectangle.class, "rect"),
    POLYGON(Polygon.class, "poly"),
    EDIT(null, "edit");

    private final Class<?> cls;
    private final String text;
    private Buttons(Class<?> _cls, String _text) {
        this.cls = _cls;
        this.text = _text;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public Class<?> shape() {
        return this.cls;
    }

    public static class ButtonSelector {
        private Buttons button;

        ButtonSelector() {
            button = null;
        }

        public void set(Buttons arg0) {
            this.button = arg0;
        }

        public Buttons get() {
            return this.button;
        }
    }
}
