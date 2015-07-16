package gov.pnnl.svf.swt.util;

import com.jogamp.opengl.swt.GLCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Utility class for getting the current state of modifier keys.
 *
 * @author Arthur Bleeker
 */
public class SwtKeyUtil implements Listener {

    private boolean shift;
    private boolean ctrl;
    private boolean alt;

    /**
     * Constructor
     *
     * @param component reference to the component
     */
    public SwtKeyUtil(final GLCanvas component) {
        component.addListener(SWT.KeyDown, this);
        component.addListener(SWT.KeyUp, this);
    }

    /**
     * @return true if the shift key is down
     */
    public boolean isShiftDown() {
        return shift;
    }

    /**
     * @return true if the control key is down
     */
    public boolean isControlDown() {
        return ctrl;
    }

    /**
     * @return true if the alt key is down
     */
    public boolean isAltDown() {
        return alt;
    }

    @Override
    public void handleEvent(final Event event) {
        if (event.type == SWT.KeyDown
            || event.type == SWT.KeyUp) {
            if (event.keyCode == SWT.SHIFT) {
                shift = event.type == SWT.KeyDown;
            } else if (event.keyCode == SWT.CONTROL) {
                ctrl = event.type == SWT.KeyDown;
            } else if (event.keyCode == SWT.ALT) {
                alt = event.type == SWT.KeyDown;
            }
        } else {
            shift = (event.stateMask & SWT.SHIFT) != 0;
            ctrl = (event.stateMask & SWT.CONTROL) != 0;
            alt = (event.stateMask & SWT.ALT) != 0;
        }
    }
}
