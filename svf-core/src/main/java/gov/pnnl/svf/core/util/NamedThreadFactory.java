package gov.pnnl.svf.core.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used for naming and counting threads created by Executors. This class
 * will append an underscore along with a number to the supplied name. It will
 * also handle and log uncaught exceptions.
 *
 * @author Arthur Bleeker
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final Logger logger = Logger.getLogger(NamedThreadFactory.class.getName());
    private final Thread.UncaughtExceptionHandler handler = new UncaughtExceptionHandlerImpl();
    private final AtomicInteger counter = new AtomicInteger();
    private final String name;
    private ClassLoader classLoader;
    private int priority = Thread.NORM_PRIORITY;

    /**
     * Constructor
     *
     * @param type the class type that created the thread factory
     */
    public NamedThreadFactory(final Class<?> type) {
        this(NamedThreadFactory.createThreadName(type));
    }

    /**
     * Constructor
     *
     * @param type   the class type that created the thread factory
     * @param suffix appended to the end of the type name using an underscore
     */
    public NamedThreadFactory(final Class<?> type, final String suffix) {
        this(NamedThreadFactory.createThreadName(type) + "_" + suffix);
    }

    /**
     * Constructor
     *
     * @param name the name of the thread
     *
     * @throws NullPointerException     if name is null
     * @throws IllegalArgumentException if the name is empty
     */
    public NamedThreadFactory(final String name) {
        super();
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name");
        }
        this.name = name;
    }

    /**
     * The context class loader to set for all threads created by this factory.
     *
     * @return the class loader or null
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * The context class loader to set for all threads created by this factory.
     * The class loader should be set before any threads are created using this
     * factory.
     *
     * @param classLoader the class loader or null
     */
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * The thread priority to set for all threads created by this factory.
     *
     * @return the thread priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * The thread priority to set for all threads created by this factory. The
     * priority must include or be between Thread.MIN_PRIORITY and
     * Thread.MAX_PRIORITY. The thread priority should be set before any threads
     * are created using this factory.
     *
     * @param priority the thread priority
     *
     * @throws IllegalArgumentException if priority is out of range
     */
    public void setPriority(final int priority) {
        if (priority <= Thread.MIN_PRIORITY || priority >= Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("priority");
        }
        this.priority = priority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = new Thread(runnable, String.format("%s_%04d", name, counter.getAndIncrement()));
        thread.setPriority(priority);
        thread.setUncaughtExceptionHandler(handler);
        thread.setDaemon(true); // not making this a daemon thread will prevent SWT from shutting down
        if (classLoader != null) {
            thread.setContextClassLoader(classLoader);
        }
        return thread;
    }

    private static String createThreadName(final Class<?> type) {
        final String[] parts = type.getName().split("\\.");
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("type");
        }
        return parts[parts.length - 1];
    }

    private static class UncaughtExceptionHandlerImpl implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(final Thread thread, final Throwable ex) {
            logger.log(Level.SEVERE, "Uncaught exception in thread: " + thread.getName(), ex);
        }
    }
}
