package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * This class provides vertex coloring or texture tinting for actors.
 *
 * @author Arthur Bleeker
 *
 */
public class ColorSupport extends AbstractSupport<Object> implements Drawable {

    /**
     * String representation of a field in this object.
     */
    public static final String COLOR = "color";
    private Color color = Color.WHITE;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected ColorSupport(final Actor actor) {
        super(actor);
    }

    /**
     * Creates a new support class and adds it to the actor's lookup.
     *
     * @param actor The owning actor.
     *
     * @return the newly created instance that is already added to the actor's
     *         lookup
     */
    public static ColorSupport newInstance(final Actor actor) {
        final ColorSupport instance = new ColorSupport(actor);
        actor.add(instance);
        return instance;
    }

    @Override
    public Scene getScene() {
        return getActor().getScene();
    }

    @Override
    public boolean isVisible() {
        return getActor().isVisible();
    }

    @Override
    public DrawingPass getDrawingPass() {
        return getActor().getDrawingPass();
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
        drawState.setAttrib();
        float[] c;
        synchronized (this) {
            c = color.toRgbaArray();
        }
        gl.glColor4fv(c, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, c, 0);
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    /**
     * Color values are RGBA.
     *
     * @return the color
     */
    public Color getColor() {
        synchronized (this) {
            return color;
        }
    }

    /**
     * Color values are RGBA.
     *
     * @param color the color to set
     *
     * @return a reference to the support object
     */
    public ColorSupport setColor(final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }
        final Color old;
        synchronized (this) {
            old = this.color;
            this.color = color;
        }
        getPropertyChangeSupport().firePropertyChange(COLOR, old, color);
        return this;
    }

    @Override
    public String toString() {
        synchronized (this) {
            return "ColorSupport{" + "color=" + color + '}';
        }
    }
}
