package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.support.ColorSupport;

/**
 * This class provides the ability to transition a color over a period of time.
 *
 * @author Amelia Bleeker
 *
 */
public class ColorAnimationSupport extends AbstractAnimationSupport {

    private final Color initial;
    private final Color color;

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
     * @param color    The color to animate to.
     */
    protected ColorAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat, final Color color) {
        super(actor, duration, pause, repeat);
        this.color = color;
        final ColorSupport colorable = getActor().lookup(ColorSupport.class);
        if (colorable != null) {
            initial = colorable.getColor();
        } else {
            initial = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    /**
     * Creates a new instance and add's this support class to the actor's
     * lookup.
     *
     * @param actor    The owning actor.
     * @param duration Duration of the animation in milliseconds. Set to a
     *                 negative number to create a pause before animating. {x:
     *                 0.0 &le; x &lt; &#8734;}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param color    The color to animate to.
     *
     * @return the new instance
     */
    public static ColorAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat, final Color color) {
        final ColorAnimationSupport instance = new ColorAnimationSupport(actor, duration, pause, repeat, color);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        final ColorSupport colorable = getActor().lookup(ColorSupport.class);
        if (colorable != null) {
            colorable.setColor(new Color((color.getRed() - initial.getRed()) * (float) fraction + initial.getRed(), (color.getGreen() - initial.getGreen())
                                                                                                                    * (float) fraction + initial.getGreen(), (color.getBlue() - initial.getBlue()) * (float) fraction + initial.getBlue(),
                                         (color.getAlpha() - initial.getAlpha()) * (float) fraction + initial.getAlpha()));
        }
    }

    @Override
    public String toString() {
        return "ColorAnimationSupport{" + "initial=" + initial + "color=" + color + '}';
    }
}
