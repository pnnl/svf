package gov.pnnl.svf.scene;

/**
 * Interface used to mark a class as disposable in the scene.
 *
 * @author Arthur Bleeker
 */
public interface Disposable {

    /**
     * Call this method to dispose of the object. Repeated calls should do
     * nothing.
     */
    void dispose();

    /**
     * Check if object has been disposed. This should return true immediately
     * following the dispose method call.
     *
     * @return true if this object has been disposed
     */
    boolean isDisposed();
}
