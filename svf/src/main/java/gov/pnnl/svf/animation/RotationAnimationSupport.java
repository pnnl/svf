package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.TransformSupport;

/**
 * This class provides animation for rotating an object over a period of time.
 * The rotation axis should be set on the transformation object before creating
 * this animation effect.
 *
 * @author Arthur Bleeker
 *
 */
public class RotationAnimationSupport extends AbstractAnimationSupport {

    private final double initial;
    private final double rotation;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor    The actor to animate.
     * @param duration The duration in milliseconds to animate. {x: 0.0 &le x
     *                 &lt &#8734}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le x &lt &#8734}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param rotation The target rotation.
     */
    protected RotationAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final double rotation) {
        super(actor, duration, pause, repeat);
        this.rotation = rotation;
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            initial = transformable.getRotation();
        } else {
            initial = 0.0f;
        }
    }

    /**
     * Creates a rotation animation for an actor with transformation support.
     *
     * @param actor    The actor to animate.
     * @param duration The duration in milliseconds to animate. {x: 0.0 &le x
     *                 &lt &#8734}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le x &lt &#8734}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param rotation The target rotation.
     *
     * @return RotationAnimationSupport created with the given parameters
     */
    public static RotationAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat, final double rotation) {
        final RotationAnimationSupport instance = new RotationAnimationSupport(actor, duration, pause, repeat, rotation);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            transformable.setRotation((rotation - initial) * fraction + initial);
        }
    }

    @Override
    public String toString() {
        return "RotationAnimationSupport{" + "initial=" + initial + "rotation=" + rotation + '}';
    }
}
