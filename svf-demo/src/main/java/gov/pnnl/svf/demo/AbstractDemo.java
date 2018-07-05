package gov.pnnl.svf.demo;

import com.jogamp.opengl.GLAutoDrawable;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.util.ConfigUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for an SVF demo.
 *
 *
 * @author Amelia Bleeker
 * @param <W> the window type
 * @param <C> the auto drawable type
 */
public abstract class AbstractDemo<W, C extends GLAutoDrawable> implements Demo<W, C> {

    private static final Logger logger = Logger.getLogger(AbstractDemo.class.getName());

    @Override
    public SceneBuilder createBuilder(final boolean debug) {
        SceneBuilder builder = null;
        try {
            builder = ConfigUtil.configure("svf.properties", "opengl.properties", "logging.properties");
        } catch (final IOException ex) {
            builder = ConfigUtil.configure();
            logger.log(Level.WARNING, "Unable to load scene builder from config files.  Using defaults.", ex);
        }
        builder.setBackground(Color.BLACK);
        if (debug) {
            builder.setDebug(debug);
        }
        // return new builder
        return builder;
    }
}
