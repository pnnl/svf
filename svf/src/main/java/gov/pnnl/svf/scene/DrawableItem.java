package gov.pnnl.svf.scene;

/**
 * Interface for classes that require drawing. This interface should be used to
 * mark an item for drawing in the scene lookup. Only objects with this
 * interface or actors will be drawn in the scene. Drawables don't make use of
 * the powerful features of an actor such as passes and visibility. They will
 * however, be rendered using the base transforms of any cameras in the scene.
 * They also get rendered before all other actors.
 *
 * @author Arthur Bleeker
 */
public interface DrawableItem extends Drawable {
}
