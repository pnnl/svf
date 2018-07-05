package gov.pnnl.svf.scene;

import java.io.File;

/**
 * Interface for working with screenshots in a scene.
 *
 * @author Amelia Bleeker
 */
public interface Screenshot {

    /**
     * Capture a screenshot to file.
     *
     * @param file the file to write to
     *
     * @throws NullPointerException if file is null
     */
    void capture(File file);

    /**
     * Capture a screenshot to file.
     *
     * @param file     the file to write to
     * @param callback reference to a callback
     *
     * @throws NullPointerException if file is null
     */
    void capture(File file, ScreenshotListener callback);
}
