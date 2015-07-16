package gov.pnnl.svf.scene;

/**
 * Enumeration for a scene that uses two drawing passes. Use
 * SceneDrawingPass.values().length to set the number of drawing passes. Use
 * FOREGROUND.ordinal() to set the drawing pass for each actor. This is provided
 * for convenience only. If more scene passes are required then you should
 * create your own drawing pass enumeration.
 *
 * @author Arthur Bleeker
 */
public enum SceneDrawingPass {

    /**
     * Scene drawing pass constant for actors in the foreground.
     */
    FOREGROUND,
    /**
     * Scene drawing pass constants for actors in the background.
     */
    BACKGROUND
}
