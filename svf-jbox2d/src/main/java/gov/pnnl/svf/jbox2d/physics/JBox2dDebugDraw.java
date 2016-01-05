package gov.pnnl.svf.jbox2d.physics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneExt;
import gov.pnnl.svf.text.TextRenderer;
import gov.pnnl.svf.text.TextService;
import java.awt.Font;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

/**
 * Class for drawing debug information in the scene.
 *
 * @author Arthur Bleeker
 */
public class JBox2dDebugDraw extends DebugDraw {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 18);
    private final SceneExt scene;
    private final float z;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     * @param z     the z plane to draw debug info on
     */
    public JBox2dDebugDraw(final Scene scene, final float z) {
        super(new OBBViewportTransform());
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene.getExtended();
        this.z = z;
    }

    @Override
    public void drawCircle(final Vec2 center, final float radius, final Color3f color) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{color.x, color.y, color.z, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl2.glLineWidth(1.0f);
        // draw
        final GLU glu = scene.getGLU();
        final GLUquadric quadric = glu.gluNewQuadric();//gl2.isGL2());
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_LINE);
        glu.gluQuadricNormals(quadric, GLU.GLU_NONE);
        glu.gluQuadricTexture(quadric, false);
        gl2.glPushMatrix();
        gl2.glTranslatef(center.x, center.y, z);
        glu.gluDisk(quadric, 0.0, radius, 90, 1);
        gl2.glPopMatrix();
        glu.gluDeleteQuadric(quadric);
    }

    @Override
    public void drawPoint(final Vec2 p, final float radius, final Color3f color) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{color.x, color.y, color.z, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl2.glLineWidth(radius);
        // draw
        gl2.glBegin(GL.GL_POINTS);
        gl2.glVertex3f(p.x, p.y, z);
        gl2.glEnd();
    }

    @Override
    public void drawSegment(final Vec2 p1, final Vec2 p2, final Color3f color) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{color.x, color.y, color.z, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl2.glLineWidth(1.0f);
        // draw
        gl2.glBegin(GL.GL_LINE_STRIP);
        gl2.glVertex3f(p1.x, p1.y, z);
        gl2.glVertex3f(p2.x, p2.y, z);
        gl2.glEnd();
    }

    @Override
    public void drawSolidCircle(final Vec2 center, final float radius, final Vec2 axis, final Color3f color) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{color.x, color.y, color.z, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
        gl2.glLineWidth(1.0f);
        // draw
        final GLU glu = scene.getGLU();
        final GLUquadric quadric = glu.gluNewQuadric();//gl2.isGL2());
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricTexture(quadric, true);
        gl2.glPushMatrix();
        gl2.glTranslatef(center.x, center.y, z);
        glu.gluDisk(quadric, 0.0, radius, 90, 1);
        gl2.glPopMatrix();
        glu.gluDeleteQuadric(quadric);
    }

    @Override
    public void drawSolidPolygon(final Vec2[] vertices, final int vertexCount, final Color3f color) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{color.x, color.y, color.z, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
        gl2.glLineWidth(1.0f);
        // draw
        gl2.glBegin(GL.GL_TRIANGLE_FAN);
        for (int i = 0; i < vertexCount; ++i) {
            gl2.glVertex3f(vertices[i].x, vertices[i].y, z);
        }
        gl2.glEnd();

        gl2.glBegin(GL.GL_LINE_LOOP);
        for (int i = 0; i < vertexCount; ++i) {
            gl2.glVertex3f(vertices[i].x, vertices[i].y, z);
        }
        gl2.glEnd();

    }

    @Override
    public void drawString(final float x, final float y, final String s, final Color3f color) {
        final TextService factory = scene.lookup(TextService.class);
        if (factory == null) {
            return;
        }
        // set up gl
        final TextRenderer renderer = factory.getTextRenderer(FONT);
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(color.x, color.y, color.z);
        renderer.draw(gl2, new Text2D(x, y, FONT, s));
    }

    @Override
    public void drawTransform(final Transform transform) {
        // set up gl
        final GL2 gl2 = scene.getGL().getGL2();
        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        gl2.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_EMISSION, new float[]{1.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl2.glLineWidth(1.0f);
        // draw transform
        final Vec2 p1 = new Vec2(transform.position);
        final float axisScale = 0.4f;
        gl2.glBegin(GL.GL_LINES);

        gl2.glColor3f(1.0f, 0.0f, 0.0f);
        gl2.glVertex3f(p1.x, p1.y, z);
        final Vec2 p2 = new Vec2(transform.R.col1.x, transform.R.col1.y).mulLocal(axisScale).add(p1);
        gl2.glVertex3f(p2.x, p2.y, z);

        gl2.glColor3f(0.0f, 1.0f, 0.0f);
        gl2.glVertex3f(p1.x, p1.y, z);
        final Vec2 p3 = new Vec2(transform.R.col2.x, transform.R.col2.y).mulLocal(axisScale).add(p1);
        gl2.glVertex3f(p3.x, p3.y, z);

        gl2.glEnd();

    }
}
