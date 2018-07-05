package gov.pnnl.svf.demo;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.event.PickingCameraEvent;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.picking.ItemPickableActor;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.util.Shape2DUtil;
import java.util.Map;

/**
 *
 * @author Amelia Bleeker
 */
class ItemPickingActor extends ShapeActor implements ItemPickableActor {

    ItemPickingActor(final Scene scene) {
        super(scene);
        setShape(Rectangle2D.ONE);
    }

    @Override
    public void itemPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final PickingCameraEvent event, final Object item) {
        if (item instanceof Quadrant) {
            Shape2DUtil.drawShape(gl, ((Quadrant) item).getShape());
        }
    }

    @Override
    public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
        super.colorPickingDraw(gl, glu, camera, support);
        gl.glPushAttrib(GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
        synchronized (support.getItems()) {
            for (final Map.Entry<Object, Color> entry : support.getItems().entrySet()) {
                if (entry.getKey() instanceof Quadrant) {
                    // set colors
                    final float[] c = entry.getValue().toRgbaArray();
                    gl.glColor4fv(c, 0);
                    // draw item
                    Shape2DUtil.drawShape(gl, ((Quadrant) entry.getKey()).getShape());
                }
            }
        }
        gl.glPopAttrib();
    }
}
