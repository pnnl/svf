/*
 * source url:
 * https://www.ibm.com/developerworks/community/blogs/javavisualization/entry/unittestingmemoryleaks?lang=en
 * license url: https://www.ibm.com/developerworks/community/terms/use/
 */
package gov.pnnl.svf.test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author unknown
 */
public class MemLeakTracker {

    private static final Logger logger = Logger.getLogger(MemLeakTracker.class.getName());
    private final HashMap<String, WeakReference<Object>> map = new HashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void register(final Object obj, final String id) {
        if (map.get(id) != null) {
            throw new IllegalArgumentException("Object already stored under id " + id);
        }
        map.put(id, new WeakReference<>(obj, new ReferenceQueue()));
    }

    public boolean isGarbageCollectable(final String id) {
        gc(); // ask for garbage collection
        final WeakReference<Object> ref = map.get(id);
        if (ref == null) {
            throw new RuntimeException("No object stored under id " + id);
        }
        return ref.get() == null;
    }

    public static Collection<Thread> getActiveThreads() {
        ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentGroup;
        while ((parentGroup = rootGroup.getParent()) != null) {
            rootGroup = parentGroup;
        }
        Thread[] threads = new Thread[rootGroup.activeCount()];
        while (rootGroup.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        final Set<Thread> list = new HashSet<>();
        for (final Thread thread : threads) {
            if (thread != null) {
                list.add(thread);
            }
        }
        return list;
    }

    private void gc() {
        final Runtime rt = Runtime.getRuntime();
        for (int i = 0; i < 3; i++) {
            try {
                allocateMemory((int) (2e6));
            } catch (final Throwable ex) {
                logger.log(Level.WARNING, null, ex);
            }
            for (int j = 0; j < 3; j++) {
                rt.gc();
            }
        }
        rt.runFinalization();
        try {
            Thread.sleep(50L);
        } catch (final Throwable ex) {
            logger.log(Level.WARNING, null, ex);
        }
    }

    @SuppressWarnings("unused")
    private void allocateMemory(final int memAmount) {
        final byte[] big = new byte[memAmount];
        // Fight against clever compilers/JVMs that may not allocate
        // unless we actually use the elements of the array
        int total = 0;
        for (int i = 0; i < Math.min(10, memAmount); i++) {
            // we don't touch all the elements, would take too long.
            if (i % 2 == 0) {
                total += big[i];
            } else {
                total -= big[i];
            }
        }
    }
}
