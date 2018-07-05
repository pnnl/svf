package gov.pnnl.svf.demo;

import gov.pnnl.svf.scene.Scene;

/**
 * Interface used to implement a demo type that will load the scene.
 *
 * @author Amelia Bleeker
 */
public interface DemoLoader {

    /**
     * Loads the scene.
     *
     * @param scene a reference to the demo scene to load
     */
    void load(Scene scene);
}
