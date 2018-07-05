package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.TransformSupport;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This class provides animation of the actor's scale over a period of time.
 *
 * @author Amelia Bleeker
 *
 */
public class ScaleAnimationSupport extends AbstractAnimationSupport {

    private final Vector3D initial;
    private final Vector3D scale;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor    The owning actor.
     * @param duration Duration of the animation in milliseconds. Set to a
     *                 negative number to create a pause before animating. {x:
     *                 0.0 &le; x &lt; &#8734;}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param scale    The scale to animate to.
     */
    protected ScaleAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final Vector3D scale) {
        super(actor, duration, pause, repeat);
        this.scale = scale;
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            initial = transformable.getScale();
        } else {
            initial = new Vector3D(1.0f, 1.0f, 1.0f);
        }
    }

    /**
     * Creates a scale animation for an actor with transformation support.
     *
     * @param actor    The owning actor.
     * @param duration Duration of the animation in milliseconds. Set to a
     *                 negative number to create a pause before animating. {x:
     *                 0.0 &le; x &lt; &#8734;}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param scale    The scale to animate to.
     *
     * @return ScaleAnimationSupport created with the given parameters
     */
    public static ScaleAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat, final Vector3D scale) {
        final ScaleAnimationSupport instance = new ScaleAnimationSupport(actor, duration, pause, repeat, scale);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            transformable.setScale(new Vector3D((scale.getX() - initial.getX()) * fraction + initial.getX(), (scale.getY() - initial.getY()) * fraction
                                                                                                             + initial.getY(), (scale.getZ() - initial.getZ()) * fraction + initial.getZ()));
        }
    }

    @Override
    public String toString() {
        return "ScaleAnimationSupport{" + "initial=" + initial + "scale=" + scale + '}';
    }
}
