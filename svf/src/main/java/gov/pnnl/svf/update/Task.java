package gov.pnnl.svf.update;

import gov.pnnl.svf.scene.Disposable;
import gov.pnnl.svf.scene.Observable;
import gov.pnnl.svf.scene.Updatable;

/**
 * Interface for a task that performs work in lockstep with the UI thread in a
 * scene. Tasks should remain in the scene lookup until finished. Issues will
 * occur if tasks are removed by any means other than the task itself.
 *
 * @author Amelia Bleeker
 */
public interface Task extends Updatable, Disposable, Observable {

    /**
     * String representation of a field in this object.
     */
    String CANCELED = "canceled";
    /**
     * String representation of a field in this object.
     */
    String FINISHED = "finished";

    /**
     * Attempts to cancel the update task. If the task has not updated and is
     * successfully canceled then this method will return true.
     *
     * Otherwise it will return false. If this task is currently updating then
     * it will finish updating and set the cancel and update flag to true.
     *
     * @return true if this task was successfully canceled
     */
    boolean cancel();

    /**
     * @return true if this task has been canceled.
     */
    boolean isCanceled();

    /**
     * @return true if this task has been completed.
     */
    boolean isFinished();

    /**
     * @return true if this task is currently updating.
     */
    boolean isUpdating();
}
