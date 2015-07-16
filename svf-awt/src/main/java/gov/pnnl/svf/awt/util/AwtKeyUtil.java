package gov.pnnl.svf.awt.util;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;

/**
 * Utility class for getting the current state of modifier keys.
 *
 * @author Arthur Bleeker
 */
public class AwtKeyUtil implements AWTEventListener {

    private boolean shift;
    private boolean ctrl;
    private boolean alt;

    /**
     * Constructor
     */
    public AwtKeyUtil() {
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
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
    public void eventDispatched(final AWTEvent event) {
        if (event instanceof InputEvent) {
            final InputEvent inputEvent = (InputEvent) event;
            shift = inputEvent.isShiftDown();
            ctrl = inputEvent.isControlDown();
            alt = inputEvent.isAltDown();
        }
    }
}
