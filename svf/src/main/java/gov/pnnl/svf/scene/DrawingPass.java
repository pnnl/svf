package gov.pnnl.svf.scene;

/**
 * Enumeration indicating the type of drawing pass.
 *
 * @author Amelia Bleeker
 *
 */
public enum DrawingPass {

    /**
     * Represents no drawing pass.
     */
    NONE(0), // 0b000
    /**
     * Actors drawn as part of the scene in perspective.
     */
    SCENE(1), // 0b001
    /**
     * Actors drawn as a user interface component as orthographic.
     */
    INTERFACE(2), // 0b010
    /**
     * The scene and user interface passes.
     */
    SCENE_INTERFACE(3), // 0b011
    /**
     * Actors drawn as an overlay component will draw onto an overlay placed on
     * top of the viewport as orthographic. Overlay actors will ignore the
     * supplied camera as the overlay always draws over the entire viewport
     * area. Overlay components cannot be picked in a scene (and should improve
     * performance by not requiring the scene to redraw more often than
     * necessary.) There is always only one overlay so actors should be inserted
     * in the order that they wish to draw. By default the tooltip is drawn as
     * an overlay component.
     */
    OVERLAY(4), // 0b100
    /**
     * The scene and overlay passes.
     */
    SCENE_OVERLAY(5), // 0b101
    /**
     * User interface and overlay passes.
     */
    INTERFACE_OVERLAY(6), // 0b110
    /**
     * User interface and overlay passes.
     */
    SCENE_INTERFACE_OVERLAY(7), // 0b110
    /**
     * Color picking pass.
     */
    PICKING(8), // 0b1000
    /**
     * Color picking pass.
     */
    SCENE_PICKING(9), // 0b1001
    /**
     * Color picking pass.
     */
    INTERFACE_PICKING(10), // 0b1010
    /**
     * Color picking pass.
     */
    SCENE_INTERFACE_PICKING(11), // 0b1011
    /**
     * Color picking pass.
     */
    OVERLAY_PICKING(12), // 0b1100
    /**
     * Color picking pass.
     */
    SCENE_OVERLAY_PICKING(13), // 0b1101
    /**
     * Color picking pass.
     */
    INTERFACE_OVERLAY_PICKING(14), // 0b1110
    /**
     * Represents all of the drawing passes.
     */
    ALL(15), // 0b1111
    ;
    private final int mask;

    private DrawingPass(final int mask) {
        this.mask = mask;
    }

    /**
     * @return true if this pass includes scene
     */
    public boolean isScene() {
        return containsDrawingPass(SCENE);
    }

    /**
     * @return true if this pass includes interface
     */
    public boolean isInterface() {
        return containsDrawingPass(INTERFACE);
    }

    /**
     * @return true if this pass includes overlay
     */
    public boolean isOverlay() {
        return containsDrawingPass(OVERLAY);
    }

    /**
     * @return true if this pass includes picking
     */
    public boolean isPicking() {
        return containsDrawingPass(PICKING);
    }

    /**
     * @param drawingPass the drawing pass to add
     *
     * @return a drawing pass that represents this and the supplied drawing pass
     */
    public DrawingPass addDrawingPass(final DrawingPass drawingPass) {
        if (drawingPass == null) {
            throw new NullPointerException("drawingPass");
        }
        return DrawingPass.values()[mask | drawingPass.mask];
    }

    /**
     * @param drawingPasses the drawing passes
     *
     * @return a drawing pass that represents all of the supplied passes
     */
    public static DrawingPass getDrawingPass(final DrawingPass... drawingPasses) {
        int value = DrawingPass.NONE.mask;
        for (final DrawingPass drawingPass : drawingPasses) {
            value |= drawingPass.mask;
        }
        return DrawingPass.values()[value];
    }

    /**
     * Test this drawing pass to see if it includes the specified drawing pass
     * or alignments.
     *
     * @param drawingPass the drawing pass to test
     *
     * @return true if this drawing pass includes the given drawing pass
     *
     * @throws NullPointerException if the argument is null
     */
    public boolean containsDrawingPass(final DrawingPass drawingPass) {
        if (drawingPass == null) {
            throw new NullPointerException("drawingPass");
        }
        return (mask & drawingPass.mask) == drawingPass.mask;
    }
}
