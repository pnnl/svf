package gov.pnnl.svf.update;

import gov.pnnl.svf.core.service.BusyService;
import gov.pnnl.svf.core.service.BusyToken;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.scene.Scene;
import java.beans.PropertyChangeSupport;

/**
 * Abstract task type for all tasks. Tasks should remain in the scene lookup
 * until finished. Issues will occur if tasks are removed by any means other
 * than the task itself.
 *
 * @author Arthur Bleeker
 */
public abstract class AbstractTask implements Task {

    private static final byte CANCELED_MASK = StateUtil.getMasks()[0];
    private static final byte FINISHED_MASK = StateUtil.getMasks()[1];
    private static final byte UPDATING_MASK = StateUtil.getMasks()[2];
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[3];
    private final Scene scene;
    private final BusyService busyService;
    private BusyToken busyToken;
    private PropertyChangeSupport pcs;
    /**
     * The delay in milliseconds for this task which may be decremented during
     * the update.
     */
    private long delay;
    private byte state = 0x00;

    /**
     * Constructor
     *
     * @param scene    The scene for this update tasks.
     * @param delay    Minimum number of milliseconds to wait before running the
     *                 task.
     * @param showBusy True if this task should show as busy
     */
    protected AbstractTask(final Scene scene, final long delay, final boolean showBusy) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        if (delay < 0L) {
            throw new IllegalArgumentException("delay");
        }
        this.scene = scene;
        this.delay = delay;
        busyService = showBusy ? scene.lookup(BusyService.class) : null;
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (StateUtil.isValue(state, DISPOSED_MASK)) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        stopBusy();
    }

    /**
     * Attempts to cancel the update task. If the task has not updated and is
     * successfully canceled then this method will return true.
     *
     * Otherwise it will return false. If this task is currently updating then
     * it will finish updating and set the cancel and update flag to true.
     *
     * @return true if this task was successfully canceled
     */
    @Override
    public boolean cancel() {
        // no need to cancel a finished or canceled task
        if (isFinished() || isCanceled() || isDisposed()) {
            return false;
        }
        // set the cancel flag
        setCanceled(true);
        // if it's currently updating then set the cancel flag
        // but don't remove it from the scene yet
        if (!isUpdating()) {
            // it can be removed from the scene
            stopBusy();
            dispose();
            scene.remove(this);
        }
        return true;
    }

    @Override
    public void update(final long delta) {
        // check the time first to ensure we don't run the task too soon
        if (delay <= 0L) {
            startBusy();
            // run this task
            try {
                setUpdating(true);
                run();
            } finally {
                setUpdating(false);
                // check if this task was finished
                if (isFinished() || isCanceled()) {
                    // remove it from the scene if it is finished
                    stopBusy();
                    dispose();
                    scene.remove(this);
                }
            }
        }
        delay -= delta;
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public boolean isCanceled() {
        synchronized (this) {
            return StateUtil.isValue(state, CANCELED_MASK);
        }
    }

    @Override
    public boolean isFinished() {
        synchronized (this) {
            return StateUtil.isValue(state, FINISHED_MASK);
        }
    }

    @Override
    public boolean isUpdating() {
        synchronized (this) {
            return StateUtil.isValue(state, UPDATING_MASK);
        }
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        synchronized (this) {
            if (pcs == null) {
                pcs = new PropertyChangeSupport(this);
            }
            return pcs;
        }
    }

    /**
     * Implement this method to run this task. This method will be called during
     * the scene update after the delay has passed.
     */
    protected abstract void run();

    /**
     * Set the task canceled field.
     *
     * @param canceled the value to set
     */
    protected void setCanceled(final boolean canceled) {
        final PropertyChangeSupport temp;
        final boolean old;
        synchronized (this) {
            old = StateUtil.isValue(state, CANCELED_MASK);
            state = StateUtil.setValue(state, CANCELED_MASK, canceled);
            temp = pcs;
        }
        if (temp != null) {
            temp.firePropertyChange(CANCELED, old, canceled);
        }
    }

    /**
     * Set the task finished field.
     *
     * @param finished the value to set
     */
    protected void setFinished(final boolean finished) {
        final PropertyChangeSupport temp;
        final boolean old;
        synchronized (this) {
            old = StateUtil.isValue(state, FINISHED_MASK);
            state = StateUtil.setValue(state, FINISHED_MASK, finished);
            temp = pcs;
        }
        if (temp != null) {
            temp.firePropertyChange(FINISHED, old, finished);
        }
    }

    /**
     * Set the task updating field.
     *
     * @param updating the value to set
     */
    private void setUpdating(final boolean updating) {
        synchronized (this) {
            state = StateUtil.setValue(state, UPDATING_MASK, updating);
        }
    }

    private void startBusy() {
        if (busyService != null) {
            synchronized (this) {
                if (busyToken == null) {
                    // get busy token
                    busyToken = busyService.startBusy();
                }
            }
        }
    }

    private void stopBusy() {
        if (busyService != null) {
            synchronized (this) {
                if (busyToken != null) {
                    busyService.stopBusy(busyToken);
                }
            }
        }
    }
}
