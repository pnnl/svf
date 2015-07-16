package gov.pnnl.svf.awt.scene;

import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.TooltipActor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * AWT implementation of the tooltip actor.
 *
 * @author Arthur Bleeker
 */
public class AwtTooltipActor extends TooltipActor implements MouseMotionListener, MouseListener {

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    public AwtTooltipActor(final Scene scene) {
        super(scene);
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        getScene().getTooltip().moved(event.getX(), event.getY());
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        getScene().getTooltip().moved(event.getX(), event.getY());
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        // no operation
        // no operation
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        getScene().getTooltip().hideTooltip();
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        // no operation
        // no operation
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no operation
        // no operation
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        // no operation
        // no operation
    }
}
