package gov.pnnl.svf.test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/**
 *
 * @author D3X573
 */
public class ObjectBImpl {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String value;

    public ObjectBImpl(String value) {
        this.value = value;
    }

    public ObjectBImpl(ObjectBImpl instance) {
        this.value = instance.getValue();
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

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        synchronized (this) {
            hash = 67 * hash + Objects.hashCode(this.value);
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
        final ObjectBImpl other = (ObjectBImpl) obj;
        synchronized (this) {
            if (!Objects.equals(this.value, other.getValue())) {
                return false;
            }
        }
        return true;
    }

}
