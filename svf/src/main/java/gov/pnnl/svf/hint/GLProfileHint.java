package gov.pnnl.svf.hint;

/**
 * Hint used to request a specific GL profile.
 *
 * @author Amelia Bleeker
 */
public enum GLProfileHint {

    /**
     * The desktop OpenGL compatibility profile 4.x, with x &ge; 0, ie GL2 plus
     * GL4.<br>
     * <code>bc</code> stands for backward compatibility.
     */
    GL4bc,
    // not supported by SVF
    //    /**
    //     * The desktop OpenGL core profile 4.x, with x >= 0
    //     */
    //    GL4,
    /**
     * The desktop OpenGL compatibility profile 3.x, with x &ge; 1, ie GL2 plus
     * GL3.<br>
     * <code>bc</code> stands for backward compatibility.
     */
    GL3bc,
    // not supported by SVF
    //    /**
    //     * The desktop OpenGL core profile 3.x, with x >= 1
    //     */
    //    GL3,
    /**
     * The desktop OpenGL profile 1.x up to 3.0
     */
    GL2,
    /**
     * The embedded OpenGL profile ES 1.x, with x &ge; 0
     */
    GLES1,
    /**
     * The embedded OpenGL profile ES 2.x, with x &ge; 0
     */
    GLES2,
    // not supported by SVF
    //    /**
    //     * The embedded OpenGL profile ES 3.x, with x >= 0
    //     */
    //    GLES3,
    /**
     * The intersection of the desktop GL2 and embedded ES1 profile
     */
    GL2ES1,
    // not supported by SVF
    //    /**
    //     * The intersection of the desktop GL3, GL2 and embedded ES2 profile
    //     */
    //    GL2ES2,
    /**
     * The intersection of the desktop GL3 and GL2 profile
     */
    GL2GL3,
    // not supported by SVF
    //    /**
    //     * The intersection of the desktop GL4 and ES3 profile, available only if
    //     * either ES3 or GL4 w/ <code>GL_ARB_ES3_compatibility</code> is available.
    //     */
    //    GL4ES3,
    /**
     * The default profile, used for the device default profile map
     */
    GL_DEFAULT;
}
