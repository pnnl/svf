package gov.pnnl.svf.scene;

/**
 * Represents an item that exists in the scene.
 *
 * @author Amelia Bleeker
 */
public interface SceneItem {

    /**
     * @return a reference to the parent scene
     */
    Scene getScene();

    /**
     * @return the drawing pass for this scene item
     */
    DrawingPass getDrawingPass();

    /**
     * @return true if this scene item is visible
     */
    boolean isVisible();
}
