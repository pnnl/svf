package gov.pnnl.svf.actor;

/**
 * This class represents the visible scrollbar extent of a scrollbar actor. Its
 * primary use it as a placeholder for picking events.
 *
 * @author Amelia Bleeker
 */
public class ScrollbarKnob {

    private final AbstractScrollbarActor scrollbar;

    /**
     * Constructor
     *
     * @param scrollbar reference to the parent scrollbar
     */
    protected ScrollbarKnob(final AbstractScrollbarActor scrollbar) {
        if (scrollbar == null) {
            throw new NullPointerException("scrollbar");
        }
        this.scrollbar = scrollbar;
    }

    /**
     * A reference to the the parent scrollbar actor.
     *
     * @return the scrollbar actor
     */
    public AbstractScrollbarActor getScrollbar() {
        return scrollbar;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.scrollbar != null ? this.scrollbar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScrollbarKnob other = (ScrollbarKnob) obj;
        if (this.scrollbar != other.scrollbar && (this.scrollbar == null || !this.scrollbar.equals(other.scrollbar))) {
            return false;
        }
        return true;
    }

}
