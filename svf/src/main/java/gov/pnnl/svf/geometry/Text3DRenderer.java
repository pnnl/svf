package gov.pnnl.svf.geometry;

import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.text.TextRenderer;
import gov.pnnl.svf.text.TextService;
import gov.pnnl.svf.util.Shape3DUtil;
import javax.media.opengl.GL2;

/**
 * Shape renderer implementation.
 *
 * @author Arthur Bleeker
 */
public class Text3DRenderer extends AbstractShapeRenderer {

    private TextRenderer textRenderer;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     */
    public Text3DRenderer(final Scene scene) {
        super(scene);
    }

    @Override
    public void prepare(final GL2 gl, final Shape shape) {
        if (shape instanceof Text3D) {
            final Text3D text = (Text3D) shape;
            final TextService textService = scene.lookup(TextService.class);
            textRenderer = textService.getTextRenderer(text.getFont());
            textRenderer.prepare(gl, text.getText());
        }
    }

    @Override
    public int drawBackground(final GL2 gl, final Shape shape) {
        if (shape instanceof Text3D) {
            return Shape3DUtil.drawShape(gl, (Text3D) shape);
        }
        return 0;
    }

    @Override
    public int drawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Text3D) {
            final Text3D text = (Text3D) shape;
            textRenderer.draw(gl, text);
            return text.getText().length() * 4;
        }
        return 0;
    }

    @Override
    public int drawBorder(final GL2 gl, final Shape shape, final Border border, final double thickness) {
        if (shape instanceof Text3D) {
            return Shape3DUtil.drawBorder(gl, (Text3D) shape, border, thickness);
        }
        return 0;
    }

    @Override
    public int pickingDrawShape(final GL2 gl, final Shape shape) {
        if (shape instanceof Text3D) {
            return Shape3DUtil.drawShape(gl, (Text3D) shape);
        }
        return 0;
    }

}
