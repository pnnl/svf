package gov.pnnl.svf.newt.scene;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.TooltipActor;

/**
 * NEWT implementation of the tooltip actor.
 *
 * @author Amelia Bleeker
 */
public class NewtTooltipActor extends TooltipActor implements MouseListener {

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    public NewtTooltipActor(final Scene scene) {
        super(scene);
    }

    @Override
    public void mouseClicked(final MouseEvent evt) {
        // no operation
        // no operation
    }

    @Override
    public void mouseEntered(final MouseEvent evt) {
        // no operation
        // no operation
    }

    @Override
    public void mouseExited(final MouseEvent evt) {
        // no operation
        // no operation
    }

    @Override
    public void mousePressed(final MouseEvent evt) {
        getScene().getTooltip().hideTooltip();
    }

    @Override
    public void mouseReleased(final MouseEvent evt) {
        // no operation
        // no operation
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        getScene().getTooltip().moved(event.getX(), event.getY());
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        getScene().getTooltip().moved(event.getX(), event.getY());
    }

    @Override
    public void mouseWheelMoved(final MouseEvent evt) {
        // no operation
        // no operation
    }
}
