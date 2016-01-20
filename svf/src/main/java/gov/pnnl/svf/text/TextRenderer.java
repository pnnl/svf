package gov.pnnl.svf.text;

import com.jogamp.opengl.GL2;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.geometry.Shape;
import gov.pnnl.svf.geometry.Text;
import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.Initializable;
import java.awt.Font;

/**
 * Interface for drawing text in a scene using either orthographic or 3D
 * transformation.
 *
 * @author Arthur Bleeker
 */
public interface TextRenderer extends Initializable, Disposable {

    /**
     * @return the Font this renderer is using.
     */
    Font getFont();

    /**
     * Implement and return true to allow this text renderer to be cleaned up.
     *
     * @return true if this text renderer is expired
     */
    boolean isExpired();

    /**
     * @return true if this text renderer is dirty
     */
    boolean isDirty();

    /**
     * @return true if anti aliased
     */
    boolean isAntiAliased();

    /**
     * @return true if mip maps
     */
    boolean isMipMaps();

    /**
     * @return true if fractional metrics
     */
    boolean isFractional();

    /**
     * @return true if smoothing
     */
    boolean isSmoothing();

    /**
     * Initializes the textures and regions for the characters in this text
     * string. This is necessary to ensure that all of the characters are
     * prepared before attempting to draw in a call list.
     *
     * @param gl   reference to GL
     * @param text the text to prepare
     */
    void prepare(GL2 gl, String text);

    /**
     * Draws the supplied text at the desired location using the current color.
     * This method must be called while the GL context is active.
     *
     * @param gl   reference to GL
     * @param text the text to draw
     */
    void draw(GL2 gl, Text text);

    /**
     * Draws the supplied text at the desired location using the current color.
     * This method must be called while the GL context is active.
     *
     * @param gl        reference to GL
     * @param text      the text to draw
     * @param alignment the alignment for the text
     */
    void draw(GL2 gl, Text text, Alignment alignment);

    /**
     * Draws the supplied text at the desired location using the current color.
     * This method must be called while the GL context is active.
     *
     * @param gl        reference to GL
     * @param text      the text to draw
     * @param area      the area to align within
     * @param alignment the alignment for the text
     */
    void draw(GL2 gl, Text text, Shape area, Alignment alignment);

    /**
     * Dispose this text. This must be called with a current GL context.
     */
    @Override
    void dispose();
}
