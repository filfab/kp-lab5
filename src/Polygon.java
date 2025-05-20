public class Polygon extends javafx.scene.shape.Polygon implements Movable, Resizable, Rotatable {
    Polygon(double arg0, double arg1) {
        super(arg0, arg1, arg0, arg1);
    }

    @Override
    public void move(double arg0, double arg1) {
        //TODO: polygon move
    }

    @Override
    public void resize(double arg0) {
        //TODO: polygon resize
    }

    @Override
    public void rotate(double arg0) {
        this.setRotate(this.getRotate() + arg0 + 0.1);
    }
}
