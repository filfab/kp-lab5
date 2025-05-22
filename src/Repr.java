/**
 * An interface for 2D shape objects that defines a serializable representation.
 */
public interface Repr {
    /**
     * Constructs a serializable representation of the object.
     * 
     * @return Serialization-ready representation of the object
     */
    Utils.ShapeRepr createRepr();

    /**
     * Object scpecific recreation step.
     * 
     * @param args array of object specific parameters
     */
    void recreate(Double[] args);
}
