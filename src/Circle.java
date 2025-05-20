public class Circle extends javafx.scene.shape.Circle implements Movable, Resizable {
    Circle(double x, double y) {
        super(x, y, 0);
    }

    @Override
    public void resize(double arg0) {
        this.setRadius(this.getRadius() + arg0 * 0.1);
    }

    @Override
    public void move(double arg0, double arg1) {
        this.setCenterX(arg0);
        this.setCenterY(arg1);
    }
}
