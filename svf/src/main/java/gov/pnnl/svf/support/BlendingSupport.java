package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Drawable;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;

/**
 * This class provides a way to specify how to blend colors using alpha for
 * actors. Blending must be enabled to use this support object.
 *
 * @author Arthur Bleeker
 *
 */
public class BlendingSupport extends AbstractSupport<Object> implements Drawable {

    /**
     * String representation of a field in this object.
     */
    public static final String SOURCE_FACTOR = "sourceFactor";
    /**
     * String representation of a field in this object.
     */
    public static final String DESTINATION_FACTOR = "destinationFactor";
    /**
     * String representation of a field in this object.
     */
    public static final String SOURCE_ALPHA = "sourceAlpha";
    /**
     * String representation of a field in this object.
     */
    public static final String DESTINATION_ALPHA = "destinationAlpha";
    private BlendingType sourceFactor = BlendingType.ONE_MINUS_SRC_ALPHA;
    private BlendingType destinationFactor = BlendingType.ONE_MINUS_SRC_ALPHA;
    private BlendingType sourceAlpha = BlendingType.NONE;
    private BlendingType destinationAlpha = BlendingType.NONE;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor The owning actor.
     */
    protected BlendingSupport(final Actor actor) {
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
    public static BlendingSupport newInstance(final Actor actor) {
        final BlendingSupport instance = new BlendingSupport(actor);
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

    /**
     * @return the blending function type to use for destination alpha
     */
    public BlendingType getDestinationAlpha() {
        synchronized (this) {
            return destinationAlpha;
        }
    }

    /**
     * @param destinationAlpha the blending function type to use for destination
     *                         alpha
     *
     * @return a reference to the support object
     */
    public BlendingSupport setDestinationAlpha(final BlendingType destinationAlpha) {
        if (destinationAlpha == null) {
            throw new NullPointerException("destinationAlpha");
        }
        final BlendingType old;
        synchronized (this) {
            old = this.destinationAlpha;
            this.destinationAlpha = destinationAlpha;
        }
        getPropertyChangeSupport().firePropertyChange(DESTINATION_ALPHA, old, destinationAlpha);
        return this;
    }

    /**
     * @return the blending function type to use for destination colors (and
     *         alpha if no alpha is specified)
     */
    public BlendingType getDestinationFactor() {
        synchronized (this) {
            return destinationFactor;
        }
    }

    /**
     * @param destinationFactor the blending function type to use for
     *                          destination colors (and alpha if no alpha is
     *                          specified)
     *
     * @return a reference to the support object
     */
    public BlendingSupport setDestinationFactor(final BlendingType destinationFactor) {
        if (destinationFactor == null) {
            throw new NullPointerException("destinationFactor");
        }
        final BlendingType old;
        synchronized (this) {
            old = this.destinationFactor;
            this.destinationFactor = destinationFactor;
        }
        getPropertyChangeSupport().firePropertyChange(DESTINATION_FACTOR, old, destinationFactor);
        return this;
    }

    /**
     * @return the blending function type to use for source alpha
     */
    public BlendingType getSourceAlpha() {
        synchronized (this) {
            return sourceAlpha;
        }
    }

    /**
     * @param sourceAlpha the blending function type to use for source alpha
     *
     * @return a reference to the support object
     */
    public BlendingSupport setSourceAlpha(final BlendingType sourceAlpha) {
        if (sourceAlpha == null) {
            throw new NullPointerException("sourceAlpha");
        }
        final BlendingType old;
        synchronized (this) {
            old = this.sourceAlpha;
            this.sourceAlpha = sourceAlpha;
        }
        getPropertyChangeSupport().firePropertyChange(SOURCE_ALPHA, old, sourceAlpha);
        return this;
    }

    /**
     * @return the blending function type to use for source colors (and alpha if
     *         no alpha is specified)
     */
    public BlendingType getSourceFactor() {
        synchronized (this) {
            return sourceFactor;
        }
    }

    /**
     * @param sourceFactor the blending function type to use for source colors
     *                     (and alpha if no alpha is specified)
     *
     * @return a reference to the support object
     */
    public BlendingSupport setSourceFactor(final BlendingType sourceFactor) {
        if (sourceFactor == null) {
            throw new NullPointerException("sourceFactor");
        }
        final BlendingType old;
        synchronized (this) {
            old = this.sourceFactor;
            this.sourceFactor = sourceFactor;
        }
        getPropertyChangeSupport().firePropertyChange(SOURCE_FACTOR, old, sourceFactor);
        return this;
    }

    @Override
    public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        drawState.clearValues();
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL.GL_COLOR_BUFFER_BIT);
        drawState.setAttrib();
        synchronized (this) {
            if (sourceFactor == BlendingType.NONE || destinationFactor == BlendingType.NONE) {
                // we aren't using blending
                gl.glDisable(GL.GL_BLEND);
            } else if (sourceAlpha == BlendingType.NONE || destinationAlpha == BlendingType.NONE) {
                // use combined functions
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(sourceFactor.getGlConstant(), destinationFactor.getGlConstant());
            } else {
                // use separate functions
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFuncSeparate(sourceFactor.getGlConstant(), destinationFactor.getGlConstant(),
                                       sourceAlpha.getGlConstant(), destinationAlpha.getGlConstant());
            }
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        if (drawState.isAttrib()) {
            gl.glPopAttrib();
        }
        drawState.clearValues();
    }

    @Override
    public String toString() {
        return "BlendingSupport{" + "sourceFactor=" + sourceFactor + ", destinationFactor=" + destinationFactor + ", sourceAlpha=" + sourceAlpha
               + ", destinationAlpha=" + destinationAlpha + '}';
    }
}
