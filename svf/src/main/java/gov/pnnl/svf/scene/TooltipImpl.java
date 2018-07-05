package gov.pnnl.svf.scene;

import gov.pnnl.svf.core.color.Color;
import gov.pnnl.svf.core.geometry.Alignment;
import gov.pnnl.svf.core.geometry.Border;
import gov.pnnl.svf.core.util.PropertyChangeSupportWrapper;
import gov.pnnl.svf.core.util.StateUtil;
import gov.pnnl.svf.geometry.Rectangle;
import gov.pnnl.svf.geometry.Rectangle2D;
import gov.pnnl.svf.geometry.Text2D;
import gov.pnnl.svf.support.TransformSupport;
import gov.pnnl.svf.update.SingleUpdateTaskManager;
import gov.pnnl.svf.update.Task;
import gov.pnnl.svf.update.TaskManager;
import gov.pnnl.svf.update.UpdateTaskRunnable;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.apache.commons.math.geometry.Vector3D;

/**
 * Class for creating and working with the toolip in a scene.
 *
 * @author Amelia Bleeker
 */
public class TooltipImpl implements Tooltip {

    /**
     * State mask for boolean field in this actor.
     */
    private static final byte DISPOSED_MASK = StateUtil.getMasks()[0];
    /**
     * State used by flags in this actor to save memory space. Up to 8 states
     * can be used and should go in the following order: 0x01, 0x02, 0x04, 0x08,
     * 0x10, 0x20, 0x40, and (byte) 0x80. Set the initial state in the
     * constructor by or-ing the new states with the current state.
     */
    private byte state = StateUtil.NONE;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupportWrapper(this);
    private final PropertyChangeListener listener;
    protected final Scene scene;
    protected TooltipActor tooltip;
    private Font font = DEFAULT_FONT;
    private Color fontColor = DEFAULT_FONT_COLOR;
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Color borderColor = DEFAULT_BORDER_COLOR;
    private double borderPadding = 2.0;
    private double borderThickness = 2.0;
    private int sensitivity = 50;
    protected int x = 0;
    protected int y = 0;

    /**
     * Constructor
     *
     * @param scene reference to the scene
     *
     * @throws NullPointerException if the scene is null
     */
    protected TooltipImpl(final Scene scene) {
        if (scene == null) {
            throw new NullPointerException("scene");
        }
        this.scene = scene;
        listener = new PropertyChangeListenerImpl(this);
        propertyChangeSupport.addPropertyChangeListener(listener);
        listener.propertyChange(new PropertyChangeEvent(scene, "reset", Boolean.FALSE, Boolean.TRUE));
    }

    @Override
    public boolean isDisposed() {
        synchronized (this) {
            return StateUtil.isValue(state, DISPOSED_MASK);
        }
    }

    @Override
    public void dispose() {
        synchronized (this) {
            if (isDisposed()) {
                return;
            }
            state = StateUtil.setValue(state, DISPOSED_MASK);
        }
        final PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners();
        for (final PropertyChangeListener listener : listeners) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }
        synchronized (this) {
            if (tooltip != null) {
                tooltip.dispose();
                tooltip = null;
            }
        }
    }

    @Override
    public void showTooltip(final String text, final int x, final int y) {
        if (text == null) {
            throw new NullPointerException("text");
        }
        scene.getDefaultTaskManager().schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                final TooltipActor temp;
                synchronized (TooltipImpl.this) {
                    temp = tooltip;
                }
                if (temp == null) {
                    return true;
                }
                final TransformSupport transform = temp.lookup(TransformSupport.class);
                if (transform == null) {
                    return true;
                }
                // set the new text
                temp.setText(text);
                // get variables and calculate metrics
                final Text2D shape = (Text2D) temp.getShape();
                // bounds are upper left
                final Rectangle2D bounds = new Rectangle2D(shape.getX() - shape.getWidth() / 2.0, shape.getY() + shape.getHeight() / 2.0, shape.getWidth(),
                                                           shape.getHeight());
                final Rectangle viewport = temp.getScene().getViewport();
                // calculate x
                int tx = x;
                // move slightly right of cursor
                tx += 4;
                // move left if off the right
                if (bounds.getWidth() < viewport.getWidth() && (tx + bounds.getWidth()) > viewport.getWidth()) {
                    tx -= ((tx + bounds.getWidth()) - viewport.getWidth());
                }
                // calculate y
                int ty = (viewport.getHeight() - y);
                // move below cursor
                ty -= 24;
                // move up if off of the bottom
                if ((ty - bounds.getHeight()) < 0) {
                    ty -= (ty - bounds.getHeight());
                }
                // set tooltip location
                transform.setTranslation(new Vector3D(tx, ty, 0));
                // set the location that this tooltip is was shown last
                synchronized (TooltipImpl.this) {
                    TooltipImpl.this.x = x;
                    TooltipImpl.this.y = y;
                }
                // show this tooltip
                temp.setVisible(true);
                return true;
            }
        });
    }

    @Override
    public void hideTooltip() {
        scene.getDefaultTaskManager().schedule(new UpdateTaskRunnable() {

            @Override
            public void disposed(final Task task) {
                // no operation
            }

            @Override
            public boolean run(final Task task) {
                final TooltipActor temp;
                synchronized (TooltipImpl.this) {
                    temp = tooltip;
                }
                if (temp != null) {
                    temp.setVisible(false);
                }
                return true;
            }
        });
    }

    @Override
    public Font getFont() {
        synchronized (this) {
            return font;
        }
    }

    @Override
    public void setFont(final Font font) {
        if (font == null) {
            throw new NullPointerException("font");
        }
        final Font oldFont;
        synchronized (this) {
            oldFont = this.font;
            this.font = font;
        }
        propertyChangeSupport.firePropertyChange(FONT, oldFont, font);
    }

    @Override
    public Color getFontColor() {
        synchronized (this) {
            return fontColor;
        }
    }

    @Override
    public void setFontColor(final Color fontColor) {
        if (fontColor == null) {
            throw new NullPointerException("fontColor");
        }
        final Color oldFontColor;
        synchronized (this) {
            oldFontColor = this.fontColor;
            this.fontColor = fontColor;
        }
        propertyChangeSupport.firePropertyChange(FONT_COLOR, oldFontColor, fontColor);
    }

    @Override
    public Color getBackgroundColor() {
        synchronized (this) {
            return backgroundColor;
        }
    }

    @Override
    public void setBackgroundColor(final Color backgroundColor) {
        if (backgroundColor == null) {
            throw new NullPointerException("backgroundColor");
        }
        final Color oldBackgroundColor;
        synchronized (this) {
            oldBackgroundColor = this.backgroundColor;
            this.backgroundColor = backgroundColor;
        }
        propertyChangeSupport.firePropertyChange(BACKGROUND_COLOR, oldBackgroundColor, backgroundColor);
    }

    @Override
    public Color getBorderColor() {
        synchronized (this) {
            return borderColor;
        }
    }

    @Override
    public void setBorderColor(final Color borderColor) {
        if (borderColor == null) {
            throw new NullPointerException("borderColor");
        }
        final Color oldBorderColor;
        synchronized (this) {
            oldBorderColor = this.borderColor;
            this.borderColor = borderColor;
        }
        propertyChangeSupport.firePropertyChange(BORDER_COLOR, oldBorderColor, borderColor);
    }

    @Override
    public double getBorderPadding() {
        synchronized (this) {
            return borderPadding;
        }
    }

    @Override
    public void setBorderPadding(final double borderPadding) {
        if (borderPadding < 0.0) {
            throw new IllegalArgumentException("borderPadding");
        }
        final double oldBorderPadding;
        synchronized (this) {
            oldBorderPadding = this.borderPadding;
            this.borderPadding = borderPadding;
        }
        propertyChangeSupport.firePropertyChange(BORDER_PADDING, oldBorderPadding, borderPadding);
    }

    @Override
    public double getBorderThickness() {
        synchronized (this) {
            return borderThickness;
        }
    }

    @Override
    public void setBorderThickness(final double borderThickness) {
        if (borderThickness < 0.0) {
            throw new IllegalArgumentException("borderThickness");
        }
        final double oldBorderThickness;
        synchronized (this) {
            oldBorderThickness = this.borderThickness;
            this.borderThickness = borderThickness;
        }
        propertyChangeSupport.firePropertyChange(BORDER_THICKNESS, oldBorderThickness, borderThickness);
    }

    @Override
    public int getSensitivity() {
        synchronized (this) {
            return sensitivity;
        }
    }

    @Override
    public void setSensitivity(final int sensitivity) {
        if (sensitivity < 0) {
            throw new IllegalArgumentException("sensitivity");
        }
        final int oldSensitivity;
        synchronized (this) {
            oldSensitivity = this.sensitivity;
            this.sensitivity = sensitivity;
        }
        propertyChangeSupport.firePropertyChange(SENSITIVITY, oldSensitivity, sensitivity);
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @Override
    public void moved(final int x, final int y) {
        synchronized (this) {
            final double d = Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
            if (d > sensitivity) {
                hideTooltip();
            }
        }
    }

    private static class PropertyChangeListenerImpl implements PropertyChangeListener, UpdateTaskRunnable {

        private final TooltipImpl instance;
        private final TaskManager taskManager;

        private PropertyChangeListenerImpl(final TooltipImpl instance) {
            if (instance == null) {
                throw new NullPointerException("scene");
            }
            this.instance = instance;
            taskManager = new SingleUpdateTaskManager(instance.scene);
        }

        @Override
        public void disposed(final Task task) {
            // no operation
        }

        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            taskManager.schedule(this, 100L);
        }

        @Override
        public boolean run(final Task task) {
            synchronized (instance) {
                instance.tooltip = instance.updateTooltip(instance.tooltip);
            }
            return true;
        }
    }

    /**
     * Update the tooltip and create a new tooltip actor if necessary.
     *
     * @param tooltip the tooltip to update with
     *
     * @return the existing or new tooltip actor
     */
    protected TooltipActor updateTooltip(TooltipActor tooltip) {
        if (tooltip == null) {
            // just reuse the parameter since we are validating it here
            tooltip = scene.getFactory().createTooltip(scene);
            tooltip.setVisible(false);
            TransformSupport.newInstance(tooltip);
            scene.add(tooltip);
        }
        // tooltip fields
        tooltip.setDrawingPass(DrawingPass.OVERLAY);
        tooltip.setOrigin(Alignment.LEFT_TOP);
        tooltip.setBorder(Border.ALL);
        synchronized (this) {
            tooltip.setFont(font);
            tooltip.setColor(fontColor);
            tooltip.setBackgroundColor(backgroundColor);
            tooltip.setBorderColor(borderColor);
            //            tooltip.setBorderPadding(borderPadding);
            tooltip.setBorderThickness(borderThickness);
        }
        // return the valid instance
        return tooltip;
    }
}
