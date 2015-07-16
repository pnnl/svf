package gov.pnnl.svf.hint;

/**
 * Hint used to determine file based configuration used in the scene. File based
 * configuration properties specified will override any default and auto
 * configuration. These hints should be supported by the scene creator.
 *
 * @author Arthur Bleeker
 */
public enum FileConfigHint {

    /**
     * Utilize file based configuration for OpenGL.
     */
    OPENGL,
    /**
     * Utilize file based configuration for SVF.
     */
    SVF,
    /**
     * Utilize file based configuration for logging.
     */
    LOGGING;
}
