/**
 * An interface for 2D shape objects that can be resized.
 */
public interface Resizable {

    /**
     * Resizes the object by a given factor or delta.
     * 
     * @param amount the amount by which to resize; positive to increase, negative to decrease
     */
    void resize(double amount);
}
