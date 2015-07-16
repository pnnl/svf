package gov.pnnl.svf.awt.picking;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.actor.ShapeActor;
import gov.pnnl.svf.awt.AwtTestScene;
import gov.pnnl.svf.camera.Camera;
import gov.pnnl.svf.core.collections.KeyValuePair;
import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.picking.ColorPickingCamera;
import gov.pnnl.svf.picking.ColorPickingSupport;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.support.ColorSupport;
import gov.pnnl.svf.support.TransformSupport;
import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.gl2.GLUgl2;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class ColorPickingTest {

    private static final Logger logger = Logger.getLogger(ColorPickingTest.class.getName());
    public static final int SPACING = 2;
    private static final int PADDING = 10;
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    public static final int SELECT_SIZE = 8;
    private static final AwtTestScene scene = new AwtTestScene(WIDTH * SPACING + PADDING * 2 + 50, HEIGHT * SPACING + PADDING * 2 + 50);

    /**
     * Set up for testing
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        scene.setUp();
        // populate
        final PlaneActorImpl actor = new PlaneActorImpl(scene);
        actor.setDrawingPass(DrawingPass.INTERFACE);
        ColorSupport.newInstance(actor).setColor(Color.WHITE);
        TransformSupport.newInstance(actor);
        final ColorPickingSupport cps = ColorPickingSupport.newInstance(actor);
        cps.clearMapping(actor);
        for (int x = 0; x < ColorPickingTest.WIDTH; x++) {
            for (int y = 0; y < ColorPickingTest.HEIGHT; y++) {
                cps.newMapping(new Point(x * SPACING + SPACING / 2, y * SPACING + SPACING / 2));
            }
        }
        scene.add(actor);
    }

    /**
     * Clean up after testing
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        scene.tearDown();
    }

    @Test
    public void testInitialization() {
        final ColorPickingCamera picking = scene.lookup(ColorPickingCamera.class);
        Assert.assertNotNull(picking);
        final Actor actor = scene.getActor("Pickable");
        Assert.assertNotNull(actor);
        final ColorPickingSupport cps = actor.lookup(ColorPickingSupport.class);
        Assert.assertNotNull(cps);
    }

    //    @Test
    public void testColorPicking() {
        // wait for everything to initialize and draw at least once
        final ColorPickingCamera picking = scene.lookup(ColorPickingCamera.class);
        final Actor actor = scene.getActor("Pickable");
        final ColorPickingSupport cps = actor.lookup(ColorPickingSupport.class);
        // wait for actor to initialize and draw
        try {
            Thread.sleep(100L);
        } catch (final InterruptedException ex) {
            // ignore
        }
        while (actor.isDirty()) {
            try {
                Thread.sleep(100L);
            } catch (final InterruptedException ex) {
                // ignore
            }
        }
        // check if this test can even be run (won't run on build server because OpenGL is to weak)
        if (scene.getGLProfile().isGL2()) {
            logger.log(Level.INFO, "Running color picking on OpenGL version: {0}", scene.getGLProfile().getImplName());
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    //                System.out.println("Checking x: " + x + " and y: " + y);
                    Assert.assertEquals(new KeyValuePair<ColorPickingSupport, Object>(cps, new Point(x * SPACING + SPACING / 2, y * SPACING + SPACING / 2)),
                                        picking.checkPickingHit(PADDING + x * SPACING + SPACING / 2, PADDING + y * SPACING + SPACING / 2));
                }
            }
        } else {
            logger.log(Level.WARNING, "Cannot run color picking on OpenGL version: {0}", scene.getGLProfile().getImplName());
        }
    }

    //    @Test
    public void testColorPickingArea() {
        // wait for everything to initialize and draw at least once
        final ColorPickingCamera picking = scene.lookup(ColorPickingCamera.class);
        final Actor actor = scene.getActor("Pickable");
        final ColorPickingSupport cps = actor.lookup(ColorPickingSupport.class);
        // wait for actor to initialize and draw
        try {
            Thread.sleep(100L);
        } catch (final InterruptedException ex) {
            // ignore
        }
        while (actor.isDirty()) {
            try {
                Thread.sleep(100L);
            } catch (final InterruptedException ex) {
                // ignore
            }
        }
        // check if this test can even be run (won't run on build server because OpenGL is to weak)
        if (scene.getGLProfile().isGL2()) {
            logger.log(Level.INFO, "Running color picking on OpenGL version: {0}", scene.getGLProfile().getImplName());
            final Set<KeyValuePair<ColorPickingSupport, Object>> picks = new HashSet<>();
            for (int x = 0; x < SELECT_SIZE; x++) {
                for (int y = 0; y < SELECT_SIZE; y++) {
                    picks.add(new KeyValuePair<ColorPickingSupport, Object>(cps, new Point(x * SPACING + SPACING / 2, y * SPACING + SPACING / 2)));
                }
            }
            final Set<KeyValuePair<ColorPickingSupport, Object>> actual;
            final int size = SELECT_SIZE * SPACING - SPACING;
            actual = picking.checkPickingHit(PADDING + size / 2 + SPACING / 2,
                                             PADDING + size / 2 + SPACING / 2,
                                             size, size);
            Assert.assertEquals(picks, actual);
        } else {
            logger.log(Level.WARNING, "Cannot run color picking on OpenGL version: {0}", scene.getGLProfile().getImplName());
        }
    }

    static class PlaneActorImpl extends ShapeActor {

        private static final double[][] NORMALS = new double[][]{{0.0, 0.0, 1.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, 1.0}};
        private final double[][] verts = new double[][]{{-0.5, -0.5, 0.0}, {-0.5, 0.5, 0.0}, {0.5, 0.5, 0.0}, {0.5, -0.5, 0.0}};

        PlaneActorImpl(final Scene scene) {
            super(scene, "Pickable");
            setShape(new Rectangle2D(1.0, 1.0));
        }

        @Override
        public void draw(final GL2 gl, final GLUgl2 glu, final Camera camera) {
            gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);
            gl.glDisable(GL.GL_MULTISAMPLE);
            gl.glDisable(GL2ES1.GL_POINT_SMOOTH);
            gl.glDisable(GL.GL_LINE_SMOOTH);
            gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
            gl.glDisable(GL.GL_BLEND);
            gl.glDisable(GL.GL_DITHER);
            gl.glDisable(GL2ES1.GL_FOG);
            gl.glDisable(GLLightingFunc.GL_LIGHTING);
            gl.glDisable(GL2GL3.GL_TEXTURE_1D);
            gl.glDisable(GL.GL_TEXTURE_2D);
            gl.glDisable(GL2ES2.GL_TEXTURE_3D);
            gl.glShadeModel(GLLightingFunc.GL_FLAT);
            colorPickingDraw(gl, glu, camera, lookup(ColorPickingSupport.class));
            gl.glPopAttrib();
        }

        @Override
        public void colorPickingDraw(final GL2 gl, final GLUgl2 glu, final Camera camera, final ColorPickingSupport support) {
            if (support == null) {
                setDirty(true);
                return;
            }
            final int[] viewport = new int[4];
            gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
            final Map<Object, Color> items = support.getItems();
            synchronized (items) {
                for (final Entry<Object, Color> entry : items.entrySet()) {
                    if (entry.getKey() instanceof Point) {
                        final Point l = (Point) entry.getKey();
                        final float[] c = entry.getValue().toRgbaArray();
                        gl.glColor4fv(c, 0);
                        verts[0][0] = PADDING + l.x - 1.0;
                        verts[0][1] = viewport[3] - PADDING - l.y - 1.0;
                        verts[1][0] = PADDING + l.x - 1.0;
                        verts[1][1] = viewport[3] - PADDING - l.y + 1.0;
                        verts[2][0] = PADDING + l.x + 1.0;
                        verts[2][1] = viewport[3] - PADDING - l.y + 1.0;
                        verts[3][0] = PADDING + l.x + 1.0;
                        verts[3][1] = viewport[3] - PADDING - l.y - 1.0;
                        drawPolygon(gl, 0, 3, 2, 1);
                    }
                }
            }
        }

        private void drawPolygon(final GL2 gl, final int v1, final int v2, final int v3, final int v4) {
            // create a square polygon
            gl.glBegin(GL2.GL_POLYGON);
            // vertex one
            gl.glNormal3dv(NORMALS[v1], 0);
            gl.glVertex3dv(verts[v1], 0);
            // vertex two
            gl.glNormal3dv(NORMALS[v2], 0);
            gl.glVertex3dv(verts[v2], 0);
            // vertex three
            gl.glNormal3dv(NORMALS[v3], 0);
            gl.glVertex3dv(verts[v3], 0);
            // vertex four
            gl.glNormal3dv(NORMALS[v4], 0);
            gl.glVertex3dv(verts[v4], 0);
            // end draw
            gl.glEnd();
        }
    }
}
