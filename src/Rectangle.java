public class Rectangle extends javafx.scene.shape.Rectangle implements Movable, Resizable, Rotatable {
    double pivotX, pivotY;

    Rectangle(double x, double y) {
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
}
