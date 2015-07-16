package gov.pnnl.svf.swt.scene;

import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.TooltipActor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

/**
 * SWT implementation of the tooltip actor.
 *
 * @author Arthur Bleeker
 */
public class SwtTooltipActor extends TooltipActor implements MouseMoveListener, MouseListener {

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    public SwtTooltipActor(final Scene scene) {
        super(scene);
    }

    @Override
    public void mouseMove(final MouseEvent event) {
        getScene().getTooltip().moved(event.x, event.y);
    }

    @Override
    public void mouseDoubleClick(final MouseEvent event) {
        // no operation
        // no operation
    }

    @Override
    public void mouseDown(final MouseEvent event) {
        getScene().getTooltip().hideTooltip();
    }

    @Override
    public void mouseUp(final MouseEvent event) {
        // no operation
        // no operation
    }
}
