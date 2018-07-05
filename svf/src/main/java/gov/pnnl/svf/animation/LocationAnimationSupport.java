package gov.pnnl.svf.animation;

import gov.pnnl.svf.camera.Camera;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This class provides a means to animate the location of the camera over a
 * period of time.
 *
 * @author Amelia Bleeker
 *
 */
public class LocationAnimationSupport extends AbstractAnimationSupport {

    private final Vector3D initial;
    private final Vector3D location;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param camera   The owning camera.
     * @param duration Duration of the animation in milliseconds. Set to a
     *                 negative number to create a pause before animating. {x:
     *                 0.0 &le; x &lt; &#8734;}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param location The location to animate to.
     */
    protected LocationAnimationSupport(final Camera camera, final long duration, final long pause, final boolean repeat, final Vector3D location) {
        super(camera, duration, pause, repeat);
        this.location = location;
        initial = camera.getLocation();
    }

    /**
     * Constructor
     *
     * @param camera   The owning camera.
     * @param duration Duration of the animation in milliseconds. Set to a
     *                 negative number to create a pause before animating. {x:
     *                 0.0 &le; x &lt; &#8734;}
     * @param pause    The time in milliseconds to wait before the animation
     *                 starts. {x: 0.0 &le; x &lt; &#8734;}
     * @param repeat   Set to true to repeat this animation. Remove this object
     *                 from lookup to stop animation.
     * @param location The location to animate to.
     *
     * @return TranslationAnimationSupport created with the given parameters
     */
    public static LocationAnimationSupport newInstance(final Camera camera, final long duration, final long pause, final boolean repeat,
                                                       final Vector3D location) {
        final LocationAnimationSupport instance = new LocationAnimationSupport(camera, duration, pause, repeat, location);
        camera.add(instance);
        return instance;
    }

    @Override
    protected void animate(final double fraction) {
        ((Camera) getActor()).setLocation(new Vector3D((location.getX() - initial.getX()) * fraction + initial.getX(), (location.getY() - initial.getY())
                                                                                                                       * fraction + initial.getY(), (location.getZ() - initial.getZ()) * fraction + initial.getZ()));
    }

    @Override
    public String toString() {
        return "LocationAnimationSupport{" + "initial=" + initial + "location=" + location + '}';
    }
}
