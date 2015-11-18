package gov.pnnl.svf.animation;

/**
 * Listener used to listen for events during animation.
 *
 * @author Arthur Bleeker
 */
public interface AnimationSupportListener {

    /**
     * Use this method to receive notification when the current iteration is
     * completed.
     */
    void iterationCompleted();

    /**
     * Use this method to receive notification when the animation is completed.
     */
    void animationCompleted();
}
