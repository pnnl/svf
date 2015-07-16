package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;

/**
 * This class provides the ability to transition a certain line thickness over a
 * period of time.
 *
 * @author Arthur Bleeker
 *
 */
public class ThicknessAnimationSupport extends AbstractAnimationSupport {

    private final float initial;
    private final float thickness;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor     The owning actor.
     * @param duration  Duration of the animation in milliseconds. Set to a
     *                  negative number to create a pause before animating. {x:
     *                  0.0 &le x &lt &#8734}
     * @param pause     The time in milliseconds to wait before the animation
     *                  starts. {x: 0.0 &le x &lt &#8734}
     * @param repeat    Set to true to repeat this animation. Remove this object
     *                  from lookup to stop animation.
     * @param thickness The line thickness to animate to.
     */
    protected ThicknessAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final float thickness) {
        super(actor, duration, pause, repeat);
        this.thickness = thickness;
        initial = actor.getThickness();
    }

    /**
     * Creates a new instance and add's this support class to the actor's
     * lookup.
     *
     * @param actor     The owning actor.
     * @param duration  Duration of the animation in milliseconds. Set to a
     *                  negative number to create a pause before animating. {x:
     *                  0.0 &le x &lt &#8734}
     * @param pause     The time in milliseconds to wait before the animation
     *                  starts. {x: 0.0 &le x &lt &#8734}
     * @param repeat    Set to true to repeat this animation. Remove this object
     *                  from lookup to stop animation.
     * @param thickness The line thickness to animate to.
     *
     * @return the new instance
     */
    public static ThicknessAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat, final float thickness) {
        final ThicknessAnimationSupport instance = new ThicknessAnimationSupport(actor, duration, pause, repeat, thickness);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        getActor().setThickness((thickness - initial) * (float) fraction + initial);
    }

    @Override
    public String toString() {
        return "ThicknessAnimationSupport{" + "initial=" + initial + "thickness=" + thickness + '}';
    }
}
