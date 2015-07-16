package gov.pnnl.svf.newt.actor;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import gov.pnnl.svf.actor.AbstractScrollbarActor;
import gov.pnnl.svf.scene.Scene;

/**
 * An actor that represents a horizontal scrollbar. Vertical scrollbar can be
 * created by rotating the actor. Minimum and maximum represent the total area.
 * The scrollbar knob represents the range from <code>(value) to (value +
 * extent)</code>. The minimum size of the rendered scroll bar knob will be a
 * square centered on the value. Interactions for the scrollbar need to be
 * implemented. Ensure that the knob is added to the picking support in order to
 * receive picking events for it. The instance must also be added as a listener
 * to item or color picking support.
 *
 * @author Arthur Bleeker
 */
public class NewtScrollbarActor extends AbstractScrollbarActor implements MouseListener {

    /**
     * Default Constructor
     *
     * @param scene Reference to the scene that this actor belongs to. The scene
     *              will be added to the lookup.
     * @param type  The type of actor.
     * @param id    The unique id for this actor.
     *
     * @throws NullPointerException     if any parameters are null
     * @throws IllegalArgumentException if type or id are empty
     */
    public NewtScrollbarActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        // no operation;
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        released();
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseExited(final MouseEvent event) {
        exited();
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        dragged(event.getX(), event.getY());
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        moved(event.getX(), event.getY());
    }

    @Override
    public void mouseWheelMoved(final MouseEvent event) {
        // no operation
    }
}
