package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.scene.Updatable;
import gov.pnnl.svf.support.AbstractSupport;

public abstract class AbstractAnimationSupport extends AbstractSupport<AnimationSupportListener> implements Updatable {

    /**
     * String representation of a field in this object.
     */
    public static final String STEP = "step";
    private final long duration;
    private final boolean repeat;
    private long step = 0L;

    /**
     * Constructor
     *
     * @param actor    The actor that is being animated.
     * @param duration Amount of time in milliseconds for the animation. {x: 0
     *                 &le x &lt &#8734}
     * @param pause    The time in milliseconds to wait before the animation {x:
     *                 0.0 &le x &lt &#8734} starts.
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     */
    protected AbstractAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat) {
        super(actor);
        this.duration = duration < 0L ? 0L : duration;
        this.repeat = repeat;
        step = pause < 0L ? 0L : -pause;
    }

    @Override
    public void addInstance(final Actor actor) {
        throw new UnsupportedOperationException("This support object type does not support multiple actors.");
    }

    /**
     * Implement this function to handle the animation. This function is called
     * when the animation is started, during updates, and won't be called when
     * the animation is finished. This animation will also be removed from the
     * lookup when completed.
     *
     * @param fraction the current fraction (0 to 1) of the animation
     */
    protected abstract void animate(double fraction);

    @Override
    public void update(final long delta) {
        final long old = step;
        step += delta;
        if (step > duration) {
            step = duration;
        }
        // only animate if the step is not negative
        // negative step indicates a pause
        if (step >= 0L) {
            final double fraction = (double) step / (double) duration;
            animate(fraction);
            // dirty this object
            getPropertyChangeSupport().firePropertyChange(STEP, old, step);
            if (step >= duration) {
                if (!repeat) {
                    // animation is finished
                    getActor().remove(this);
                    // deregister this object in the scene
                    getActor().getScene().remove(this);
                    for (final AnimationSupportListener listener : getListeners()) {
                        listener.animationCompleted();
                    }
                } else {
                    // repeat this animation
                    step -= duration;
                    for (final AnimationSupportListener listener : getListeners()) {
                        listener.iterationCompleted();
                    }
                }
            }
        }
    }
}
