package gov.pnnl.svf.hint;

/**
 * Hint used to determine auto configuration used in the scene. Auto
 * configuration will override default configuration. These hints should be
 * supported by the scene creator.
 *
 * @author Arthur Bleeker
 */
public enum AutoConfigHint {

    /**
     * Utilize auto configuration for OpenGL.
     */
    OPENGL,
    /**
     * Utilize auto configuration for SVF.
     */
    SVF,
    /**
     * Utilize auto configuration for logging.
     */
    LOGGING;
}
