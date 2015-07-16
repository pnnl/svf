package gov.pnnl.svf.camera;

import gov.pnnl.svf.actor.Actor;
import gov.pnnl.svf.core.util.Transformer;
import gov.pnnl.svf.geometry.Frustum;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.scene.DrawingPass;
import gov.pnnl.svf.scene.Scene;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Set;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.gl2.GLUgl2;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Camera used to count vertices.
 *
 * @author Arthur Bleeker
 */
public class CountingCamera implements CameraExt<Object> {

    private long verticesCounter = 0;

    /**
     * Constructor
     */
    public CountingCamera() {
    }

    @Override
    public long getVerticesCounter() {
        return verticesCounter;
    }

    @Override
    public void incrementVerticesCounter(final long count) {
        verticesCounter += count;
    }

    @Override
    public void resetVerticesCounter() {
        verticesCounter = 0L;
    }

    @Override
    public Vector3D getCameraUp() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double getFarClip() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double getFieldOfView() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Vector3D getLocation() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Vector3D getLook() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public double getNearClip() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Vector3D getPerp() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Vector3D getUp() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Rectangle getViewport() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Transformer<Rectangle> getViewportTransformer() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Frustum getFrustum() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setLookAt(GL2 gl, GLUgl2 glu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setViewport(Rectangle viewport) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setViewportTransformer(Transformer<Rectangle> transformer) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setLocation(Vector3D location) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setNearClip(double nearClip) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setFarClip(double farClip) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setFieldOfView(double fieldOfView) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setLook(Vector3D look) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera setUp(Vector3D up) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public CameraExt<?> getExtended() {
        return (CameraExt) this;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Camera getCamera() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public byte getPassNumber() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public float getThickness() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public String getType() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isDirty() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isRoot() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isWire() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setCamera(Camera camera) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setDirty(boolean dirty) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setDrawingPass(DrawingPass drawingPass) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setPassNumber(byte passNumber) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setPassNumber(int passNumber) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setThickness(float thickness) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setType(String type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setVisible(boolean visible) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Actor setWire(boolean wire) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public <T> void add(T object) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public <T> T lookup(Class<? extends T> type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public <T> boolean remove(T object) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Set<Object> lookupAll() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void lookupAll(Collection<Object> out) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void draw(GL2 gl, GLUgl2 glu, Camera camera) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void endDraw(GL2 gl, GLUgl2 glu, Camera camera) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Scene getScene() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public DrawingPass getDrawingPass() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isVisible() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isDisposed() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void makeOrtho2D(GL2 gl, GLUgl2 glu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void makePerspective(GL2 gl, GLUgl2 glu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void endOrtho2D(GL2 gl, GLUgl2 glu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void endPerspective(GL2 gl, GLUgl2 glu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void addListener(Object listener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void removeListener(Object listener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void clearListeners() {
        throw new UnsupportedOperationException("Not supported.");
    }

}
