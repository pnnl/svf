package gov.pnnl.svf.util;

import gov.pnnl.svf.OffscreenTestScene;
import gov.pnnl.svf.core.util.MathUtil;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.UpdateTaskAdapter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import com.jogamp.opengl.GL2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class MathUtilTest {

    private static final OffscreenTestScene scene = new OffscreenTestScene();
    private static final double[][] TRANSLATIONS = new double[][]{
        {0.0, 0.0, 0.0},
        {-0.0, -0.0, -0.0},
        {1.0, 0.0, 0.0},
        {-1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0},
        {0.0, -1.0, 0.0},
        {0.0, 0.0, 1.0},
        {0.0, 0.0, -1.0},
        {1.0, 2.0, 3.0},
        {-1.0, -2.0, -3.0},
        {2.0, 1.0, 3.0},
        {-2.0, -1.0, -3.0},
        {3.0, 2.0, 1.0},
        {-3.0, -2.0, -1.0}
    };
    private static final double[][] SCALES = new double[][]{
        {0.0, 0.0, 0.0},
        {-0.0, -0.0, -0.0},
        {1.0, 0.0, 0.0},
        {-1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0},
        {0.0, -1.0, 0.0},
        {0.0, 0.0, 1.0},
        {0.0, 0.0, -1.0},
        {1.0, 2.0, 3.0},
        {-1.0, -2.0, -3.0},
        {2.0, 1.0, 3.0},
        {-2.0, -1.0, -3.0},
        {3.0, 2.0, 1.0},
        {-3.0, -2.0, -1.0}
    };
    private static final double[][] ROTATIONS = new double[][]{
        {1.0, 0.0, 0.0},
        {-1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0},
        {0.0, -1.0, 0.0},
        {0.0, 0.0, 1.0},
        {0.0, 0.0, -1.0},
        {Math.sqrt(1.0), Math.sqrt(1.0), 0.0},
        {-Math.sqrt(1.0), -Math.sqrt(1.0), 0.0},
        {0.0, Math.sqrt(1.0), Math.sqrt(1.0)},
        {0.0, -Math.sqrt(1.0), -Math.sqrt(1.0)},
        {Math.sqrt(1.0), 0.0, Math.sqrt(1.0)},
        {-Math.sqrt(1.0), 0.0, -Math.sqrt(1.0)}
    };
    private static final double[] ANGLES = new double[]{
        0.0,
        -0.0,
        360.0,
        -360.0,
        45.0,
        -45.0,
        180.0,
        -180.0,
        90.0,
        -90.0,
        123.456,
        -123.456,
        0.987654321,
        -0.987654321,};

    /**
     * Set up for testing
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        scene.setUp();
    }

    /**
     * Clean up after testing
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        scene.tearDown();
    }

    /**
     * Test of newIdentityMatrix method, of class MathUtil.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testNewIdentityMatrix() throws Exception {
        final AtomicReference<AssertionError> fault = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        scene.getDefaultTaskManager().schedule(new UpdateTaskAdapter() {

            @Override
            public boolean run(final Task task) {
                try {
                    final GL2 gl = scene.getGL().getGL2();
                    gl.getContext().makeCurrent();
                    // opengl route
                    final double[] opengl = new double[16];
                    gl.glPushMatrix();
                    gl.glLoadIdentity();
                    gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, opengl, 0);
                    gl.glPopMatrix();
                    // normalize zeroes
                    for (int i = 0; i < opengl.length; i++) {
                        if (opengl[i] == -0.0) {
                            opengl[i] = 0.0;
                        }
                    }
                    // math util
                    final double[] mathutil = MathUtil.newIdentityMatrix();
                    // compare
//                    System.out.println("OpenGL: \n" + MathUtil.toString(opengl));
//                    System.out.println("MathUtil: \n" + MathUtil.toString(mathutil));
                    Assert.assertArrayEquals(opengl, mathutil, 0.000001);
                    gl.getContext().release();
                } catch (final AssertionError ex) {
                    fault.set(ex);
                } finally {
                    latch.countDown();
                }
                return true;
            }
        });
        if (!latch.await(1, TimeUnit.MINUTES)) {
            Assert.fail("Timed Out");
        }
        if (fault.get() != null) {
            throw fault.get();
        }
    }

    @Test
    public void testCompareToOpenGL() throws Exception {
        final AtomicReference<AssertionError> fault = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        scene.getDefaultTaskManager().schedule(new UpdateTaskAdapter() {

            @Override
            public boolean run(final Task task) {
                try {
                    final GL2 gl = scene.getGL().getGL2();
                    gl.getContext().makeCurrent();
                    for (int t = 0; t < TRANSLATIONS.length; t++) {
                        final double[] translation = TRANSLATIONS[t];
                        for (int r = 0; r < ROTATIONS.length; r++) {
                            final double[] rotation = ROTATIONS[r];
                            for (int a = 0; a < ANGLES.length; a++) {
                                final double angle = ANGLES[a];
                                for (int s = 0; s < SCALES.length; s++) {
                                    final double[] scale = SCALES[s];
                                    // opengl route
                                    final double[] opengl = new double[16];
                                    gl.glPushMatrix();
                                    gl.glLoadIdentity();
                                    gl.glTranslated(translation[GeometryUtil.X], translation[GeometryUtil.Y], translation[GeometryUtil.Z]);
                                    gl.glRotated(angle, rotation[GeometryUtil.X], rotation[GeometryUtil.Y], rotation[GeometryUtil.Z]);
                                    gl.glScaled(scale[GeometryUtil.X], scale[GeometryUtil.Y], scale[GeometryUtil.Z]);
                                    gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, opengl, 0);
                                    gl.glPopMatrix();
                                    // normalize zeroes
                                    for (int i = 0; i < opengl.length; i++) {
                                        if (opengl[i] == -0.0) {
                                            opengl[i] = 0.0;
                                        }
                                    }
                                    // math util
                                    final double[] mathutil = MathUtil.newIdentityMatrix();
                                    MathUtil.translate(mathutil, translation[GeometryUtil.X], translation[GeometryUtil.Y], translation[GeometryUtil.Z]);
                                    MathUtil.rotate(mathutil, angle, rotation[GeometryUtil.X], rotation[GeometryUtil.Y], rotation[GeometryUtil.Z]);
                                    MathUtil.scale(mathutil, scale[GeometryUtil.X], scale[GeometryUtil.Y], scale[GeometryUtil.Z]);
                                    // compare
//                                    System.out.println("OpenGL: \n" + MathUtil.toString(opengl));
//                                    System.out.println("MathUtil: \n" + MathUtil.toString(mathutil));
                                    Assert.assertArrayEquals(opengl, mathutil, 0.000001);
                                }
                            }
                        }
                    }
                    gl.getContext().release();
                } catch (final AssertionError ex) {
                    fault.set(ex);
                } finally {
                    latch.countDown();
                }
                return true;
            }
        });
        if (!latch.await(1, TimeUnit.MINUTES)) {
            Assert.fail("Timed Out");
        }
        if (fault.get() != null) {
            throw fault.get();
        }
    }

    /**
     * Test of index method, of class MathUtil.
     */
    @Test
    public void testIndex() {
        final double[] matrix = new double[]{
            1.0, 2.0, 3.0, 4.0,
            5.0, 6.0, 7.0, 8.0,
            9.0, 10.0, 11.0, 12.0,
            13.0, 14.0, 15.0, 16.0
        };
        // row, column order is reversed in OpenGL
        Assert.assertEquals(1.0, matrix[MathUtil.index(0, 0)], 0.0);
        Assert.assertEquals(2.0, matrix[MathUtil.index(1, 0)], 0.0);
        Assert.assertEquals(3.0, matrix[MathUtil.index(2, 0)], 0.0);
        Assert.assertEquals(4.0, matrix[MathUtil.index(3, 0)], 0.0);
        Assert.assertEquals(5.0, matrix[MathUtil.index(0, 1)], 0.0);
        Assert.assertEquals(6.0, matrix[MathUtil.index(1, 1)], 0.0);
        Assert.assertEquals(7.0, matrix[MathUtil.index(2, 1)], 0.0);
        Assert.assertEquals(8.0, matrix[MathUtil.index(3, 1)], 0.0);
        Assert.assertEquals(9.0, matrix[MathUtil.index(0, 2)], 0.0);
        Assert.assertEquals(10.0, matrix[MathUtil.index(1, 2)], 0.0);
        Assert.assertEquals(11.0, matrix[MathUtil.index(2, 2)], 0.0);
        Assert.assertEquals(12.0, matrix[MathUtil.index(3, 2)], 0.0);
        Assert.assertEquals(13.0, matrix[MathUtil.index(0, 3)], 0.0);
        Assert.assertEquals(14.0, matrix[MathUtil.index(1, 3)], 0.0);
        Assert.assertEquals(15.0, matrix[MathUtil.index(2, 3)], 0.0);
        Assert.assertEquals(16.0, matrix[MathUtil.index(3, 3)], 0.0);
    }

}
