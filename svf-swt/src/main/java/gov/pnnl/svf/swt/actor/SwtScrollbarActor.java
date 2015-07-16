package gov.pnnl.svf.swt.actor;

import gov.pnnl.svf.actor.AbstractScrollbarActor;
import gov.pnnl.svf.scene.Scene;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;

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
public class SwtScrollbarActor extends AbstractScrollbarActor implements MouseListener, MouseMoveListener, DragDetectListener, MouseTrackListener, MouseWheelListener {

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
    public SwtScrollbarActor(final Scene scene, final String type, final String id) {
        super(scene, type, id);
    }

    @Override
    public void mouseDoubleClick(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseDown(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseUp(final MouseEvent event) {
        released();
    }

    @Override
    public void mouseEnter(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseExit(final MouseEvent event) {
        exited();
    }

    @Override
    public void mouseHover(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseScrolled(final MouseEvent event) {
        // no operation
    }

    @Override
    public void mouseMove(final MouseEvent event) {
        moved(event.x, event.y);
    }

    @Override
    public void dragDetected(final DragDetectEvent event) {
        dragged(event.x, event.y);
    }
}
