package gov.pnnl.svf.event;

import gov.pnnl.svf.core.util.StateUtil;

/**
 * Event type used for a proxy camera.
 *
 * @author Amelia Bleeker
 */
public class ProxyMouseEvent {

    /**
     * Proxy mouse event type.
     */
    public static enum Type {

        BUTTON1,
        BUTTON2,
        BUTTON3,
        WHEEL;
    }

    private static final byte ALT_MASK = StateUtil.getMasks()[0];
    private static final byte CONTROL_MASK = StateUtil.getMasks()[1];
    private static final byte SHIFT_MASK = StateUtil.getMasks()[2];
    private final int x;
    private final int y;
    private final Type button;
    private final int clickCount;
    private final int wheelRotation;
    private final byte state;

    /**
     * Create a proxy mouse button event.
     *
     * @param button     the mouse button
     * @param clickCount the click count
     * @param x          the x location, left to right
     * @param y          the y location, top to bottom
     * @param alt        true for alt down
     * @param control    true for control down
     * @param shift      true for shift down
     */
    public ProxyMouseEvent(final Type button, final int clickCount, final int x, final int y, final boolean alt, final boolean control, final boolean shift) {
        this(button, clickCount, 0, x, y, alt, control, shift);
        if (button == Type.WHEEL) {
            throw new IllegalArgumentException("button");
        }
        if (clickCount < 1) {
            throw new IllegalArgumentException("clickCount");
        }
    }

    /**
     * Create a proxy mouse wheel event.
     *
     * @param wheelRotation the mouse wheel rotation, positive for up, negative
     *                      for down
     * @param x             the x location, left to right
     * @param y             the y location, top to bottom
     * @param alt           true for alt down
     * @param control       true for control down
     * @param shift         true for shift down
     */
    public ProxyMouseEvent(final int wheelRotation, final int x, final int y, final boolean alt, final boolean control, final boolean shift) {
        this(Type.WHEEL, 0, wheelRotation, x, y, alt, control, shift);
    }

    private ProxyMouseEvent(final Type button, final int clickCount, final int wheelRotation, final int x, final int y, final boolean alt,
                            final boolean control, final boolean shift) {
        if (button == null) {
            throw new NullPointerException("button");
        }
        this.button = button;
        this.x = x;
        this.y = y;
        this.clickCount = clickCount;
        this.wheelRotation = wheelRotation;
        byte temp = 0;
        temp = StateUtil.setValue(temp, ALT_MASK, alt);
        temp = StateUtil.setValue(temp, CONTROL_MASK, control);
        temp = StateUtil.setValue(temp, SHIFT_MASK, shift);
        state = temp;
    }

    /**
     * @return the button type
     */
    public Type getButton() {
        return button;
    }

    /**
     * @return the x location
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y location
     */
    public int getY() {
        return y;
    }

    /**
     * @return the button click count
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * @return the wheel rotation
     */
    public int getWheelRotation() {
        return wheelRotation;
    }

    /**
     * @return true if alt is down
     */
    public boolean isAltDown() {
        return StateUtil.isValue(state, ALT_MASK);
    }

    /**
     * @return true if control is down
     */
    public boolean isControlDown() {
        return StateUtil.isValue(state, CONTROL_MASK);
    }

    /**
     * @return true if shift is down
     */
    public boolean isShiftDown() {
        return StateUtil.isValue(state, SHIFT_MASK);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + x;
        hash = 67 * hash + y;
        hash = 67 * hash + (button != null ? button.hashCode() : 0);
        hash = 67 * hash + clickCount;
        hash = 67 * hash + wheelRotation;
        hash = 67 * hash + state;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProxyMouseEvent other = (ProxyMouseEvent) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        if (button != other.button) {
            return false;
        }
        if (clickCount != other.clickCount) {
            return false;
        }
        if (wheelRotation != other.wheelRotation) {
            return false;
        }
        if (state != other.state) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProxyMouseEvent{" + "x=" + x + ", y=" + y + ", button=" + button + ", wheelRotation=" + wheelRotation + ", alt=" + isAltDown() + ", control="
               + isControlDown() + ", shift=" + isShiftDown() + '}';
    }

    public static class Builder {

        private int x = 0;
        private int y = 0;
        private Type button = null;
        private int clickCount = 0;
        private int wheelRotation = 0;
        private boolean alt = false;
        private boolean control = false;
        private boolean shift = false;

        private Builder() {
        }

        public static Builder construct() {
            return new Builder();
        }

        public Builder x(final int x) {
            this.x = x;
            return this;
        }

        public Builder y(final int y) {
            this.y = y;
            return this;
        }

        public Builder button(final Type button) {
            this.button = button;
            return this;
        }

        public Builder clickCount(final int clickCount) {
            this.clickCount = clickCount;
            return this;
        }

        public Builder wheelRotation(final int wheelRotation) {
            this.wheelRotation = wheelRotation;
            return this;
        }

        public Builder alt(final boolean alt) {
            this.alt = alt;
            return this;
        }

        public Builder control(final boolean control) {
            this.control = control;
            return this;
        }

        public Builder shift(final boolean shift) {
            this.shift = shift;
            return this;
        }

        public ProxyMouseEvent build() {
            if (button == Type.WHEEL) {
                return new ProxyMouseEvent(wheelRotation, x, y, alt, control, shift);
            } else {
                return new ProxyMouseEvent(button, clickCount, x, y, alt, control, shift);
            }
        }
    }

}
