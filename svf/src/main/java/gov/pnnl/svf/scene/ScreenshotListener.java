package gov.pnnl.svf.scene;

import java.io.File;

/**
 * Listener utilized for screen shot callback.
 *
 * @author Amelia Bleeker
 */
public interface ScreenshotListener {

    /**
     * Called after the screen shot has been successfully written to disk.
     *
     * @param file the file
     */
    void succeeded(File file);

    /**
     * Called after the screen shot failed to be written to disk.
     *
     * @param file the file
     * @param ex   the fault
     */
    void failed(File file, Exception ex);
}
