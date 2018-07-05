package gov.pnnl.svf.core.service;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base implementation of the busy service. This service will need to
 * be extended by individual framework implementations.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractBusyService implements BusyService {

    /**
     * The amount of time in milliseconds to wait before displaying a busy
     * cursor.
     */
    private static final long BUSY_WAIT_LENGTH = 1000L;
    /**
     * The amount of time in milliseconds to poll for busy token changes.
     */
    private static final long BUSY_POLLING_INTERVAL = 500L;
    protected final AtomicLong counter = new AtomicLong();
    protected final LinkedList<BusyTokenImpl> queue = new LinkedList<>();
    protected final Timer timer = new Timer("BusyService_Timer", true);
    protected final TimerTask task = new TimerTaskImpl(this);

    /**
     * Constructor
     */
    protected AbstractBusyService() {
        super();
    }

    @Override
    public void initialize() {
        timer.scheduleAtFixedRate(task, BUSY_POLLING_INTERVAL, BUSY_POLLING_INTERVAL);
    }

    @Override
    public void dispose() {
        timer.cancel();
        showBusy(false);
    }

    @Override
    public BusyToken startBusy() {
        final BusyTokenImpl token = new BusyTokenImpl(counter.getAndIncrement());
        synchronized (queue) {
            queue.add(token);
        }
        return token;
    }

    @Override
    public void stopBusy(final BusyToken busyToken) {
        if (busyToken == null) {
            throw new NullPointerException("busyToken");
        }
        synchronized (queue) {
            queue.remove(busyToken);
        }
    }

    @Override
    public void clearBusy() {
        synchronized (queue) {
            queue.clear();
        }
    }

    /**
     * Implementation of this method should show the application as busy or not
     * busy according to the supplied value.
     *
     * @param value true if application should indicate that it is busy
     */
    protected abstract void showBusy(boolean value);

    /**
     * Task used to check whether to display a busy cursor.
     *
     * @author Amelia Bleeker
     */
    protected static final class TimerTaskImpl extends TimerTask {

        private static final Logger logger = Logger.getLogger(AbstractBusyService.TimerTaskImpl.class.toString());
        private final AbstractBusyService service;

        /**
         * Constructor
         *
         * @param service reference to the parent service
         */
        protected TimerTaskImpl(final AbstractBusyService service) {
            this.service = service;
        }

        @Override
        public void run() {
            final BusyTokenImpl token;
            synchronized (service.queue) {
                token = service.queue.peek();
            }
            try {
                if (token != null && System.currentTimeMillis() > token.getTimestamp() + BUSY_WAIT_LENGTH) {
                    service.showBusy(true);
                } else {
                    service.showBusy(false);
                }
            } catch (final RuntimeException ex) {
                AbstractBusyService.TimerTaskImpl.logger.log(Level.INFO, "Unable to handle busy token.");
            }
        }
    }
}
