package gov.pnnl.svf.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLReadBufferUtil;
import gov.pnnl.svf.core.util.NamedThreadFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the Screenshot interface.
 *
 * @author Arthur Bleeker
 */
public class ScreenshotImpl implements Screenshot {

    private static final Logger logger = Logger.getLogger(ScreenshotImpl.class.getName());
    private final Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory(Screenshot.class));
    private Set<File> captures = new HashSet<>();
    private Map<File, List<ScreenshotListener>> callbacks = new HashMap<>();

    /**
     * Constructor
     */
    public ScreenshotImpl() {
    }

    /**
     * Check for waiting captures and create new screen shots if necessary.
     *
     * @param gl reference to current gl
     */
    public void dispatch(final GL2 gl) {
        synchronized (this) {
            if (!captures.isEmpty()) {
                // read the pixels
                final GLReadBufferUtil util = new GLReadBufferUtil(false, false);
                util.readPixels(gl, false);
                // gather current state
                final Set<File> captures = this.captures;
                this.captures = new HashSet<>();
                final Map<File, List<ScreenshotListener>> callbacks;
                if (!this.callbacks.isEmpty()) {
                    callbacks = this.callbacks;
                    this.callbacks = new HashMap<>();
                } else {
                    callbacks = Collections.emptyMap();
                }
                // write the files to disk on a worker thread
                executor.execute(new RunnableImpl(util, captures, callbacks));
            }
        }
    }

    @Override
    public void capture(final File file) {
        capture(file, null);
    }

    @Override
    public void capture(final File file, final ScreenshotListener callback) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        synchronized (this) {
            captures.add(file);
            if (callback != null) {
                List<ScreenshotListener> list = callbacks.get(file);
                if (list == null) {
                    list = new ArrayList<>(1);
                    callbacks.put(file, list);
                }
                list.add(callback);
            }
        }
    }

    /**
     * Worker for writing screen shots to file.
     */
    protected static class RunnableImpl implements Runnable {

        private final GLReadBufferUtil util;
        private final Set<File> captures;
        private final Map<File, List<ScreenshotListener>> callbacks;

        protected RunnableImpl(final GLReadBufferUtil util, final Set<File> captures, final Map<File, List<ScreenshotListener>> callbacks) {
            this.util = util;
            this.captures = captures;
            this.callbacks = callbacks;
        }

        @Override
        public void run() {
            for (final File file : captures) {
                try {
                    util.write(file);
                    // notify callbacks of success
                    final List<ScreenshotListener> list = callbacks.get(file);
                    if (list != null) {
                        for (final ScreenshotListener listener : list) {
                            listener.succeeded(file);
                        }
                    }
                } catch (final RuntimeException ex) {
                    // notify callbacks of failure
                    final List<ScreenshotListener> list = callbacks.get(file);
                    if (list != null) {
                        for (final ScreenshotListener listener : list) {
                            listener.failed(file, ex);
                        }
                    } else {
                        logger.log(Level.WARNING, "Unable to capture screenshot.", ex);
                    }
                }
            }
            // does not require a current context without writing to a texture
            util.dispose(null);
        }
    }

}
