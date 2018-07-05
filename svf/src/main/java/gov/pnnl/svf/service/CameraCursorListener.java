package gov.pnnl.svf.service;

import gov.pnnl.svf.camera.DraggingCamera;
import gov.pnnl.svf.core.service.CursorService;
import gov.pnnl.svf.core.service.CursorType;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listener used to respond to camera events that trigger cursor changes.
 *
 * @author Amelia Bleeker
 */
public class CameraCursorListener implements PropertyChangeListener {

    private final CursorService cursorService;

    /**
     * Constructor
     *
     * @param cursorService reference to the cursor service
     */
    protected CameraCursorListener(final CursorService cursorService) {
        if (cursorService == null) {
            throw new NullPointerException("cursorService");
        }
        this.cursorService = cursorService;
    }

    /**
     * Creates a new instance and listens to events on the given camera.
     *
     * @param cursorService reference to the cursor service
     * @param camera        reference to a dragging camera
     *
     * @return the new instance
     */
    public static CameraCursorListener newInstance(final CursorService cursorService, final DraggingCamera camera) {
        final CameraCursorListener instance = new CameraCursorListener(cursorService);
        camera.getPropertyChangeSupport().addPropertyChangeListener(instance);
        return instance;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (DraggingCamera.DRAGGING.equals(evt.getPropertyName())) {
            final Boolean dragging = (Boolean) evt.getNewValue();
            if (dragging) {
                cursorService.addActiveCursorType(CursorType.DRAGGING);
            } else {
                cursorService.removeActiveCursorType(CursorType.DRAGGING);
            }
        } else if (DraggingCamera.ZOOMING.equals(evt.getPropertyName())) {
            final Boolean dragging = (Boolean) evt.getNewValue();
            if (dragging) {
                cursorService.addActiveCursorType(CursorType.ZOOMING);
            } else {
                cursorService.removeActiveCursorType(CursorType.ZOOMING);
            }
        }
    }

}
