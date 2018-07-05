package gov.pnnl.svf.service;

import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;

/**
 * Abstract implementation of a drawable service.
 *
 * @author Amelia Bleeker
 */
public abstract class AbstractDrawableService implements DrawableService {

    private final Scene scene;
    private final int priority;

    /**
     * Constructor
     *
     * @param scene    reference to the parent scene
     * @param priority the priority for this service
     */
    public AbstractDrawableService(final Scene scene, final int priority) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(final DrawableService drawableService) {
        // sort from highest to lowest (descending)
        final int otherPriority = drawableService.getPriority();
        return (priority < otherPriority ? 1 : (priority == otherPriority ? 0 : -1));
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public DrawingPass getDrawingPass() {
        return DrawingPass.ALL;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + priority;
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
        final AbstractDrawableService other = (AbstractDrawableService) obj;
        if (priority != other.priority) {
            return false;
        }
        return true;
    }
}
