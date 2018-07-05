package gov.pnnl.svf.support;

import gov.pnnl.svf.actor.Actor;
import java.util.EnumSet;
import java.util.Set;

/**
 * Default implementation of AbstractStateSupport which has three states:
 * selected, highlighted, and related. This class can be used at a template for
 * creating your own state support classes.
 *
 * @author Amelia Bleeker
 */
public class SelectionStateSupport extends AbstractStateSupport<SelectionState> {

    private SelectionStateSupport(final Actor actor) {
        this(actor, EnumSet.allOf(SelectionState.class));
    }

    private SelectionStateSupport(final Actor actor, final Set<SelectionState> states) {
        super(actor, states);
    }

    /**
     * Constructor
     *
     * @param actor reference to the parent actor
     *
     * @return a new instance of this support object
     */
    public static SelectionStateSupport newInstance(final Actor actor) {
        return new SelectionStateSupport(actor);
    }

    /**
     * Constructor
     *
     * @param actor  reference to the parent actor
     * @param states set of states allowed for this support object
     *
     * @return a new instance of this support object
     */
    public static SelectionStateSupport newInstance(final Actor actor, final Set<SelectionState> states) {
        return new SelectionStateSupport(actor, states);
    }
}
