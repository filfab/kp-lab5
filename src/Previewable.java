/**
 * An interface for 2D shape objects that can show a temporary or preview state,
 * such as during interactive drawing or resizing.
 */
public interface Previewable {

    /**
     * Updates the preview bounds or dimensions based on the provided coordinates.
     * 
     * @param x the X coordinate representing one corner or reference point
     * @param y the Y coordinate representing the opposite corner or reference point
     */
    void preview(double arg0, double arg1);
}
