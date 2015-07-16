package gov.pnnl.svf.swt.camera;

import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;

/**
 * Interface for consolidating all of the SWT camera interfaces.
 *
 * @author Arthur Bleeker
 */
public interface SwtCamera extends MouseListener, MouseMoveListener, DragDetectListener, MouseWheelListener, MouseTrackListener {
}
