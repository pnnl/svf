package gov.pnnl.svf.scene;

/**
 * Interface for classes that require updating.
 *
 * @author Arthur Bleeker
 *
 */
public interface Updatable {

    /**
     * Called during the update cycle.
     *
     * @param delta Time elapsed in milliseconds since last update.
     */
    void update(long delta);
}
