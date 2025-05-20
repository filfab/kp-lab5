public class Circle extends javafx.scene.shape.Circle implements Movable, Resizable, Previewable {
    public Circle(double x, double y) {
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

    @Override
    public void preview(double arg0, double arg1) {
        this.setRadius(Utils.distance(this.getCenterX(), this.getCenterY(), arg0, arg1));
    }

}
