package gov.pnnl.svf.scene;

import gov.pnnl.svf.actor.DynamicBorderedShapeActor;
import gov.pnnl.svf.geometry.Text2D;
import java.awt.Font;

/**
 * Base implementation of the tooltip actor.
 *
 * @author Amelia Bleeker
 */
public class TooltipActor extends DynamicBorderedShapeActor {

    /**
     * Id and type used to identify the tooltip in the scene.
     */
    public static final String TOOLTIP = "tooltip-actor";

    /**
     * Constructor
     *
     * @param scene The parent scene for the actor.
     */
    protected TooltipActor(final Scene scene) {
        super(scene, TOOLTIP, TOOLTIP);
        setShape(Text2D.ZERO);
    }

    /**
     * Get the tooltip text.
     *
     * @return the tooltip text
     */
    public String getText() {
        return ((Text2D) getShape()).getText();
    }

    /**
     * Set the tooltip text.
     *
     * @param text the tooltip text
     */
    public void setText(final String text) {
        final Font font = ((Text2D) getShape()).getFont();
        setShape(new Text2D(font, text));
    }

    /**
     * Get the tooltip font.
     *
     * @return the tooltip font
     */
    public Font getFont() {
        return ((Text2D) getShape()).getFont();
    }

    /**
     * Set the tooltip font.
     *
     * @param font the tooltip font
     */
    public void setFont(final Font font) {
        final String text = ((Text2D) getShape()).getText();
        setShape(new Text2D(font, text));
    }
}
