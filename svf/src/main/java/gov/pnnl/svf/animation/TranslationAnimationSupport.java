package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.support.TransformSupport;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This class provides a means to animate the translation (location) of the
 * actor over a period of time.
 *
 * @author Arthur Bleeker
 *
 */
public class TranslationAnimationSupport extends AbstractAnimationSupport {

    private final Vector3D initial;
    private final Vector3D translation;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param actor       The owning actor.
     * @param duration    Duration of the animation in milliseconds. Set to a
     *                    negative number to create a pause before animating.
     *                    {x: 0.0 &le; x &lt; &#8734;}
     * @param pause       The time in milliseconds to wait before the animation
     *                    starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat      Set to true to repeat this animation. Remove this
     *                    object from lookup to stop animation.
     * @param translation The translation to animate to.
     */
    protected TranslationAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final Vector3D translation) {
        super(actor, duration, pause, repeat);
        this.translation = translation;
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            initial = transformable.getTranslation();
        } else {
            initial = new Vector3D(0.0f, 0.0f, 0.0f);
        }
    }

    /**
     * Constructor
     *
     * @param actor       The owning actor.
     * @param duration    Duration of the animation in milliseconds. Set to a
     *                    negative number to create a pause before animating.
     *                    {x: 0.0 &le; x &lt; &#8734;}
     * @param pause       The time in milliseconds to wait before the animation
     *                    starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat      Set to true to repeat this animation. Remove this
     *                    object from lookup to stop animation.
     * @param translation The translation to animate to.
     *
     * @return TranslationAnimationSupport created with the given parameters
     */
    public static TranslationAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat,
                                                          final Vector3D translation) {
        final TranslationAnimationSupport instance = new TranslationAnimationSupport(actor, duration, pause, repeat, translation);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        final TransformSupport transformable = getActor().lookup(TransformSupport.class);
        if (transformable != null) {
            transformable.setTranslation(new Vector3D((translation.getX() - initial.getX()) * fraction + initial.getX(), (translation.getY() - initial.getY())
                                                                                                                         * fraction + initial.getY(), (translation.getZ() - initial.getZ()) * fraction + initial.getZ()));
        }
    }

    @Override
    public String toString() {
        return "TranslationAnimationSupport{" + "initial=" + initial + "translation=" + translation + '}';
    }
}
