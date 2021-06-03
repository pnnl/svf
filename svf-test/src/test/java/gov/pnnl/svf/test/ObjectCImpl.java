package gov.pnnl.svf.test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author D3X573
 */
public class ObjectCImpl implements Serializable {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String value;
    private long valueLong;
    private int valueInt;
    private short valueShort;
    private byte valueByte;
    private double valueDouble;
    private float valueFloat;
    private boolean valueBoolean;
    private final Map<String, Object> map = new HashMap<>();
    private final List<String> list = new ArrayList<>();
    private final long[] arrayLong = new long[1];
    private final int[] arrayInt = new int[1];
    private final short[] arrayShort = new short[1];
    private final byte[] arrayByte = new byte[1];
    private final double[] arrayDouble = new double[1];
    private final boolean[] arrayFloat = new boolean[1];

    public ObjectCImpl(String value) {
        this.value = value;
    }

    public ObjectCImpl(ObjectCImpl instance) {
        this.value = instance.getValue();
        this.valueLong = instance.getValueLong();
        this.valueInt = instance.getValueInt();
        this.valueShort = instance.getValueShort();
        this.valueByte = instance.getValueByte();
        this.valueDouble = instance.getValueDouble();
        this.valueFloat = instance.getValueFloat();
        this.valueBoolean = instance.isValueBoolean();
    }

    public String getValue() {
        synchronized (this) {
            return value;
        }
    }

    public void setValue(String value) {
        final String old;
        synchronized (this) {
            old = this.value;
            this.value = value;
        }
        propertyChangeSupport.firePropertyChange("value", old, value);
    }

    public long getValueLong() {
        synchronized (this) {
            return valueLong;
        }
    }

    public void setValueLong(long valueLong) {
        final long old;
        synchronized (this) {
            old = this.valueLong;
            this.valueLong = valueLong;
        }
        propertyChangeSupport.firePropertyChange("valueLong", old, valueLong);
    }

    public int getValueInt() {
        synchronized (this) {
            return valueInt;
        }
    }

    public void setValueInt(int valueInt) {
        final int old;
        synchronized (this) {
            old = this.valueInt;
            this.valueInt = valueInt;
        }
        propertyChangeSupport.firePropertyChange("valueInt", old, valueInt);
    }

    public short getValueShort() {
        synchronized (this) {
            return valueShort;
        }
    }

    public void setValueShort(short valueShort) {
        final short old;
        synchronized (this) {
            old = this.valueShort;
            this.valueShort = valueShort;
        }
        propertyChangeSupport.firePropertyChange("valueShort", old, valueShort);
    }

    public byte getValueByte() {
        synchronized (this) {
            return valueByte;
        }
    }

    public void setValueByte(byte valueByte) {
        final byte old;
        synchronized (this) {
            old = this.valueByte;
            this.valueByte = valueByte;
        }
        propertyChangeSupport.firePropertyChange("valueByte", old, valueByte);
    }

    public double getValueDouble() {
        synchronized (this) {
            return valueDouble;
        }
    }

    public void setValueDouble(double valueDouble) {
        final double old;
        synchronized (this) {
            old = this.valueDouble;
            this.valueDouble = valueDouble;
        }
        propertyChangeSupport.firePropertyChange("valueDouble", old, valueDouble);
    }

    public float getValueFloat() {
        synchronized (this) {
            return valueFloat;
        }
    }

    public void setValueFloat(float valueFloat) {
        final float old;
        synchronized (this) {
            old = this.valueFloat;
            this.valueFloat = valueFloat;
        }
        propertyChangeSupport.firePropertyChange("valueFloat", old, valueFloat);
    }

    public boolean isValueBoolean() {
        synchronized (this) {
            return valueBoolean;
        }
    }

    public void setValueBoolean(boolean valueBoolean) {
        final boolean old;
        synchronized (this) {
            old = this.valueBoolean;
            this.valueBoolean = valueBoolean;
        }
        propertyChangeSupport.firePropertyChange("valueBoolean", old, valueBoolean);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        synchronized (this) {
            hash = 53 * hash + Objects.hashCode(this.value);
            hash = 53 * hash + (int) (this.valueLong ^ (this.valueLong >>> 32));
            hash = 53 * hash + this.valueInt;
            hash = 53 * hash + this.valueShort;
            hash = 53 * hash + this.valueByte;
            hash = 53 * hash + (int) (Double.doubleToLongBits(this.valueDouble) ^ (Double.doubleToLongBits(this.valueDouble) >>> 32));
            hash = 53 * hash + Float.floatToIntBits(this.valueFloat);
            hash = 53 * hash + (this.valueBoolean ? 1 : 0);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectCImpl other = (ObjectCImpl) obj;
        synchronized (this) {
            if (this.valueLong != other.getValueLong()) {
                return false;
            }
            if (this.valueInt != other.getValueInt()) {
                return false;
            }
            if (this.valueShort != other.getValueShort()) {
                return false;
            }
            if (this.valueByte != other.getValueByte()) {
                return false;
            }
            if (Double.doubleToLongBits(this.valueDouble) != Double.doubleToLongBits(other.getValueDouble())) {
                return false;
            }
            if (Float.floatToIntBits(this.valueFloat) != Float.floatToIntBits(other.getValueFloat())) {
                return false;
            }
            if (this.valueBoolean != other.isValueBoolean()) {
                return false;
            }
            if (!Objects.equals(this.value, other.getValue())) {
                return false;
            }
        }
        return true;
    }

}
