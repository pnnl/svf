package gov.pnnl.svf.scene;

import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLCapabilitiesImmutable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLRunnable;
import java.util.ArrayList;
import java.util.List;
import jogamp.common.util.locks.RecursiveLockImplJava5;

/**
 * Mock canvas used in a proxy scene. Override annotations are commented out to
 * allow for reverse compatibility with JOGL.
 *
 * @author Amelia Bleeker
 */
public class ProxyGLCanvas implements GLAutoDrawable {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private final List<GLEventListener> glEventListeners = new ArrayList<>();
    private final RecursiveLock lock = new RecursiveLockImplJava5(true);
    private GLAnimatorControl animator;
    private Thread thread;
    private GLContext context;
    private GL gl;
    private boolean realized = true;
    private boolean autoSwapBufferMode = false;
    private int contextCreationFlags = 0;
    private int width;
    private int height;

    /**
     * Constructor
     */
    public ProxyGLCanvas() {
        this(WIDTH, HEIGHT);
    }

    /**
     * Constructor
     *
     * @param width  the canvas width
     * @param height the canvas height
     */
    public ProxyGLCanvas(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public GLDrawable getDelegatedDrawable() {
        return this;
    }

    @Override
    public GLContext getContext() {
        return GLContext.getCurrent();
    }

    @Override
    public void addGLEventListener(final GLEventListener gl) {
        glEventListeners.add(gl);
        gl.reshape(this, 0, 0, width, height);
    }

    @Override
    public void addGLEventListener(final int i, final GLEventListener gl) throws IndexOutOfBoundsException {
        glEventListeners.add(i, gl);
        gl.reshape(this, 0, 0, width, height);
    }

    @Override
    public void setAnimator(final GLAnimatorControl glac) throws GLException {
        animator = glac;
    }

    @Override
    public GLAnimatorControl getAnimator() {
        return animator;
    }

    @Override
    public boolean invoke(final boolean bln, final GLRunnable glr) {
        glr.run(this);
        return true;
    }

    @Override
    public void destroy() {
        for (final GLEventListener glel : glEventListeners) {
            glel.dispose(this);
        }
    }

    @Override
    public void display() {
        for (final GLEventListener glel : glEventListeners) {
            glel.display(this);
        }
    }

    @Override
    public void setAutoSwapBufferMode(final boolean bln) {
        autoSwapBufferMode = bln;
    }

    @Override
    public boolean getAutoSwapBufferMode() {
        return autoSwapBufferMode;
    }

    @Override
    public void setContextCreationFlags(final int i) {
        contextCreationFlags = i;
    }

    @Override
    public int getContextCreationFlags() {
        return contextCreationFlags;
    }

    @Override
    public GLContext createContext(final GLContext glc) {
        return glc;
    }

    @Override
    public GL getGL() {
        return gl;
    }

    @Override
    public GL setGL(final GL gl) {
        final GL old = this.gl;
        this.gl = gl;
        return old;
    }

    @Override
    public Object getUpstreamWidget() {
        return null;
    }

    @Override
    public void setRealized(final boolean bln) {
        realized = bln;
    }

    @Override
    public boolean isRealized() {
        return realized;
    }

    @Override
    public void swapBuffers() throws GLException {
        // no operation
    }

    @Override
    public GLCapabilitiesImmutable getChosenGLCapabilities() {
        return new GLCapabilities(GLProfile.getDefault());
    }

    @Override
    public GLProfile getGLProfile() {
        return GLProfile.getDefault();
    }

    @Override
    public NativeSurface getNativeSurface() {
        return null;
    }

    @Override
    public long getHandle() {
        return 0L;
    }

    @Override
    public GLDrawableFactory getFactory() {
        return GLDrawableFactory.getFactory(GLProfile.getDefault());
    }

    @Override
    public GLContext setContext(final GLContext glc, final boolean bln) {
        final GLContext old = context;
        context = glc;
        return old;
    }

    @Override
    public int getGLEventListenerCount() {
        return glEventListeners.size();
    }

    @Override
    public GLEventListener getGLEventListener(final int i) throws IndexOutOfBoundsException {
        return glEventListeners.get(i);
    }

    @Override
    public boolean getGLEventListenerInitState(final GLEventListener gl) {
        return false;
    }

    @Override
    public void setGLEventListenerInitState(final GLEventListener gl, final boolean bln) {
        // no operation
    }

    @Override
    public GLEventListener disposeGLEventListener(final GLEventListener gl, final boolean bln) {
        return gl;
    }

    @Override
    public GLEventListener removeGLEventListener(final GLEventListener gl) {
        glEventListeners.remove(gl);
        return gl;
    }

    @Override
    public Thread setExclusiveContextThread(final Thread thread) throws GLException {
        final Thread old = this.thread;
        this.thread = thread;
        return old;
    }

    @Override
    public Thread getExclusiveContextThread() {
        return thread;
    }

    @Override
    public boolean invoke(final boolean bln, final List<GLRunnable> list) {
        // no operation
        for (final GLRunnable glr : list) {
            glr.run(this);
        }
        return bln;
    }

    @Override
    public boolean isGLOriented() {
        return false;
    }

    @Override
    public boolean areAllGLEventListenerInitialized() {
        return true;
    }

    @Override
    public void flushGLRunnables() {
        // no operation
    }

    @Override
    public RecursiveLock getUpstreamLock() {
        return lock;
    }

    @Override
    public boolean isThreadGLCapable() {
        return true;
    }

    @Override
    public int getSurfaceWidth() {
        return width;
    }

    @Override
    public int getSurfaceHeight() {
        return height;
    }

    @Override
    public GLCapabilitiesImmutable getRequestedGLCapabilities() {
        return getChosenGLCapabilities();
    }
}
