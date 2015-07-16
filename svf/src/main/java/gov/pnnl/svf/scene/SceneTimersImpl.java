package gov.pnnl.svf.scene;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of scene timers that retain references to times and stages of
 * the scene renderer.
 *
 * @author Arthur Bleeker
 */
public class SceneTimersImpl implements SceneTimers {

    private final AtomicLong lastDrawLength = new AtomicLong(0L);
    private final AtomicLong lastUpdateLength = new AtomicLong(0L);
    private final AtomicLong lastVerticesRendered = new AtomicLong(0L);
    private final AtomicLong lastCulledActors = new AtomicLong(0L);
    private final AtomicInteger lastAttribStackDepth = new AtomicInteger(0);
    private final AtomicInteger lastModelviewStackDepth = new AtomicInteger(0);
    private final AtomicInteger lastProjectionStackDepth = new AtomicInteger(0);
    private final AtomicInteger lastTextureStackDepth = new AtomicInteger(0);
    private final AtomicInteger lastErrorsReported = new AtomicInteger(0);
    private final AtomicReference<String> lastCollectionsInfo = new AtomicReference<>("");
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final AtomicBoolean drawing = new AtomicBoolean(false);
    private final AtomicReference<DrawingPass> currentDrawingPass = new AtomicReference<>();
    private DrawingPass repaint = DrawingPass.ALL;

    @Override
    public long getLastDrawLength() {
        return lastDrawLength.get();
    }

    @Override
    public void setLastDrawLength(final long lastDrawLength) {
        this.lastDrawLength.set(lastDrawLength);
    }

    @Override
    public long getLastUpdateLength() {
        return lastUpdateLength.get();
    }

    @Override
    public void setLastUpdateLength(final long lastUpdateLength) {
        this.lastUpdateLength.set(lastUpdateLength);
    }

    @Override
    public long getLastVerticesRendered() {
        return lastVerticesRendered.get();
    }

    @Override
    public void setLastVerticesRendered(final long lastVerticesRendered) {
        this.lastVerticesRendered.set(lastVerticesRendered);
    }

    @Override
    public long getLastCulledActors() {
        return lastCulledActors.get();
    }

    @Override
    public void setLastCulledActors(final long lastCulledActors) {
        this.lastCulledActors.set(lastCulledActors);
    }

    @Override
    public int getLastAttribStackDepth() {
        return lastAttribStackDepth.get();
    }

    @Override
    public void setLastAttribStackDepth(final int lastAttribStackDepth) {
        this.lastAttribStackDepth.set(lastAttribStackDepth);
    }

    @Override
    public int getLastModelviewStackDepth() {
        return lastModelviewStackDepth.get();
    }

    @Override
    public void setLastModelviewStackDepth(final int lastModelviewStackDepth) {
        this.lastModelviewStackDepth.set(lastModelviewStackDepth);
    }

    @Override
    public int getLastProjectionStackDepth() {
        return lastProjectionStackDepth.get();
    }

    @Override
    public void setLastProjectionStackDepth(final int lastProjectionStackDepth) {
        this.lastProjectionStackDepth.set(lastProjectionStackDepth);
    }

    @Override
    public int getLastTextureStackDepth() {
        return lastTextureStackDepth.get();
    }

    @Override
    public void setLastTextureStackDepth(final int lastTextureStackDepth) {
        this.lastTextureStackDepth.set(lastTextureStackDepth);
    }

    @Override
    public int getLastErrorsReported() {
        return lastErrorsReported.get();
    }

    @Override
    public void setLastErrorsReported(final int lastErrorsReported) {
        this.lastErrorsReported.set(lastErrorsReported);
    }

    @Override
    public boolean getUpdating() {
        return updating.get();
    }

    @Override
    public void setUpdating(final boolean updating) {
        this.updating.set(updating);
    }

    @Override
    public boolean getAndSetUpdating(final boolean updating) {
        return this.updating.getAndSet(updating);
    }

    @Override
    public boolean getDrawing() {
        return drawing.get();
    }

    @Override
    public void setDrawing(final boolean drawing) {
        this.drawing.set(drawing);
    }

    @Override
    public boolean getAndSetDrawing(final boolean drawing) {
        return this.drawing.getAndSet(drawing);
    }

    @Override
    public DrawingPass getRepaint() {
        synchronized (this) {
            return repaint;
        }
    }

    @Override
    public void setRepaint(final DrawingPass repaint) {
        synchronized (this) {
            this.repaint = repaint == DrawingPass.NONE ? DrawingPass.NONE : this.repaint.addDrawingPass(repaint);
        }
    }

    @Override
    public DrawingPass getAndSetRepaint(final DrawingPass repaint) {
        final DrawingPass old;
        synchronized (this) {
            old = this.repaint;
            this.repaint = repaint == DrawingPass.NONE ? DrawingPass.NONE : this.repaint.addDrawingPass(repaint);
        }
        return old;
    }

    @Override
    public DrawingPass getCurrentDrawingPass() {
        return currentDrawingPass.get();
    }

    @Override
    public void setCurrentDrawingPass(final DrawingPass currentDrawingPass) {
        this.currentDrawingPass.set(currentDrawingPass);
    }

    @Override
    public String getCollectionsInfo() {
        return lastCollectionsInfo.get();
    }

    @Override
    public void setCollectionsInfo(final String info) {
        lastCollectionsInfo.set(info == null ? "" : info);
    }
}
