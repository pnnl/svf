package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.TransformSupport;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This class allows for animating the roation axis in an actor with
 * TransformSupport. Typically animating the rotation axis is not recommended
 * for smooth rotation animation.
 *
 * @author Amelia Bleeker
 *
 */
public class RotationAxisAnimationSupport extends AbstractAnimationSupport {

    private final Vector3D initial;
    private final Vector3D rotationAxis;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor        The actor to animate.
     * @param duration     The duration in milliseconds to animate. {x: 0.0 &le;
     *                     x &lt; &#8734;}
     * @param pause        The time in milliseconds to wait before the animation
     *                     starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat       Set to true to repeat this animation. Remove this
     *                     object from lookup to stop animation.
     * @param rotationAxis The target rotation axis.
     */
    protected RotationAxisAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final Vector3D rotationAxis) {
        super(actor, duration, pause, repeat);
        this.rotationAxis = rotationAxis;
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            initial = transformable.getRotationAxis();
        } else {
            initial = Vector3D.ZERO;
        }
    }

    /**
     * Animation support for changing the rotation axis over a period of time.
     *
     * @param actor        The actor to animate.
     * @param duration     The duration in milliseconds to animate. {x: 0.0 &le;
     *                     x &lt; &#8734;}
     * @param pause        The time in milliseconds to wait before the animation
     *                     starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat       Set to true to repeat this animation. Remove this
     *                     object from lookup to stop animation.
     * @param rotationAxis The target rotation axis.
     *
     * @return RotationAxisAnimationSupport created with the given parameters
     */
    public static RotationAxisAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat,
                                                           final Vector3D rotationAxis) {
        final RotationAxisAnimationSupport instance = new RotationAxisAnimationSupport(actor, duration, pause, repeat, rotationAxis);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            transformable.setRotationAxis(new Vector3D((rotationAxis.getX() - initial.getX()) * fraction + initial.getX(), (rotationAxis.getY() - initial
                                                                                                                            .getY()) * fraction + initial.getY(), (rotationAxis.getZ() - initial.getZ()) * fraction + initial.getZ()));
        }
    }

    @Override
    public String toString() {
        return "RotationAxisAnimationSupport{" + "initial=" + initial + "rotationAxis=" + rotationAxis + '}';
    }
}
