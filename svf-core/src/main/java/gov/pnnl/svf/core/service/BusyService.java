package gov.pnnl.svf.core.service;

/**
 * Service used to display and hide the busy status in the application. This
 * service should be encapsulated within a try and finally to prevent orphaned
 * busy tokens.
 *
 * @author Arthur Bleeker
 */
public interface BusyService {

    /**
     * Get a busy token.
     *
     * @return a BusyToken to be used to stop the busy state
     */
    BusyToken startBusy();

    /**
     * Dispose of a busy token.
     *
     * @param busyToken the busyToken to stop
     */
    void stopBusy(BusyToken busyToken);

    /**
     * Clear all of the busy tokens and undo the busy state if necessary.
     */
    void clearBusy();

    /**
     * Initialize the service.
     */
    void initialize();

    /**
     * Dispose the service.
     */
    void dispose();
}
