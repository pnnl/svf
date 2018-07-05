package gov.pnnl.svf.util;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.service.AbstractDrawableService;

/**
 * Abstract class for all logger services.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractLogger extends AbstractDrawableService implements Updatable {

    protected final long interval;
    protected long counter = 0L;
    protected long elapsed = 0L;
    protected String text = "";
    protected String output = "";

    /**
     * Constructor
     *
     * @param scene    reference to the parent scene
     * @param priority the priority for this service
     * @param interval The interval in milliseconds to log at
     */
    protected AbstractLogger(final Scene scene, final int priority, final long interval) {
        super(scene, priority);
        if (interval < 0L) {
            throw new IllegalArgumentException("interval");
        }
        this.interval = interval;
    }

    /**
     * @return the output statement that gets printed to the logger
     */
    public String getOutput() {
        synchronized (this) {
            return output;
        }
    }

    /**
     * @return the text that is shown in the logging message
     */
    public String getText() {
        synchronized (this) {
            return text;
        }
    }

    /**
     * @param text The text to show in the logging message.
     */
    public void setText(final String text) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        synchronized (this) {
            this.text = text;
        }
    }

    @Override
    public void endDraw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
        // no operation
    }

}
