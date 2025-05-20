public class Rectangle extends javafx.scene.shape.Rectangle implements Movable, Resizable, Rotatable, Previewable {
    private double pivotX, pivotY;

    public Rectangle(double x, double y) {
        super(x, y, 0.0, 0.0);
        pivotX = x;
        pivotY = y;
    }
    
    @Override
    public void move(double arg0, double arg1) {
        this.setX(arg0 - this.getWidth()/2);
        this.setY(arg1 - this.getHeight()/2);
    }

    @Override
    public void resize(double arg0) {
        this.setWidth(this.getWidth() + arg0 * 0.1);
        this.setHeight(this.getHeight() + arg0 * 0.1);
        this.setX(this.getX() - arg0 * 0.05);
        this.setY(this.getY() - arg0 * 0.05);
    }

    @Override
    public void rotate(double arg0) {
        this.setRotate(this.getRotate() + arg0 * 0.1);
    }

    @Override
    public void preview(double arg0, double arg1) {
        this.setX(Math.min(this.pivotX, arg0));
        this.setY(Math.min(this.pivotY, arg1));
        this.setWidth(Math.abs(arg0 - Math.max(this.getX(), this.pivotX)));
        this.setHeight(Math.abs(arg1 - Math.max(this.getY(), this.pivotY)));
    }
}
