package gov.pnnl.svf.support;

/**
 * Listener used to listen to state changes in StateSupport.
 *
 * @param <T>
 *
 * @author Amelia Bleeker
 */
@SuppressWarnings("rawtypes")
public interface StateSupportListener<T extends Enum> {

    /**
     * Called when a state has been changed.
     *
     * @param event state change event
     */
    void stateChanged(StateEvent<T> event);
}
