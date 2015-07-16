package gov.pnnl.svf.animation;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.texture.MultiTexture2dSupport;
import gov.pnnl.svf.texture.TextureRegionListSupport;

/**
 * This class provides animation of the of the images in a animatable texture
 * support object.
 *
 * @author Lyndsey Franklin
 * @author Arthur Bleeker
 *
 */
public class TextureAnimationSupport extends AbstractAnimationSupport {

    /**
     * Minimize access to the constructor to protect references to this during
     * construction
     *
     * @param actor    actor that has textures to animate
     * @param duration duration in milliseconds of animation {x: 0.0 &le x &lt
     *                 &#8734}
     * @param pause    delay before beginning the animation {x: 0.0 &le x &lt
     *                 &#8734}
     * @param repeat   set to true to repeat the animation
     */
    protected TextureAnimationSupport(final Actor actor, final long duration, final long pause, final boolean repeat) {
        super(actor, duration, pause, repeat);
    }

    /**
     * Return an instance of this animation support class
     *
     * @param actor    actor to apply the animation to, the animation instance
     *                 created in this function will be added to the actor.
     * @param duration duration in milliseconds of animation {x: 0.0 &le x &lt
     *                 &#8734}
     * @param pause    delay before beginning the animation {x: 0.0 &le x &lt
     *                 &#8734}
     * @param repeat   set to true to repeat the animation
     *
     * @return new instance of a texture animation added to the given actor
     */
    public static TextureAnimationSupport newInstance(final Actor actor, final long duration, final long pause, final boolean repeat) {
        final TextureAnimationSupport instance = new TextureAnimationSupport(actor, duration, pause, repeat);
        actor.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        if (1.0 == fraction) {
            // multi texture type
            final MultiTexture2dSupport multiTexture = getActor().lookup(MultiTexture2dSupport.class);
            if (multiTexture != null) {
                multiTexture.nextIndex();
            }
            // texture region type
            final TextureRegionListSupport regionList = getActor().lookup(TextureRegionListSupport.class);
            if (regionList != null) {
                regionList.incrementCurrentRegion();
            }
        }
    }
}
