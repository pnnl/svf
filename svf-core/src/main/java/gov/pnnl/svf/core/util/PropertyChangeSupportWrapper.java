package gov.pnnl.svf.core.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * This is a utility class that can be used by beans that support bound
 * properties. You can use an instance of this class as a member field of your
 * bean and delegate various work to it.
 *
 * This class is serializable. When it is serialized it will save (and restore)
 * any listeners that are themselves serializable. Any non-serializable
 * listeners will be skipped during serialization.
 *
 * This wrapper will not fire events when both arguments are null.
 *
 * @author Amelia Bleeker
 */
public class PropertyChangeSupportWrapper extends PropertyChangeSupport {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>PropertyChangeSupportWrapper</code> object.
     *
     * @param sourceBean The bean to be given as the source for any events.
     */
    public PropertyChangeSupportWrapper(final Object sourceBean) {
        super(sourceBean);
    }

    @Override
    public void firePropertyChange(final PropertyChangeEvent evt) {
        if (evt.getOldValue() == null && evt.getNewValue() == null) {
            return;
        }
        super.firePropertyChange(evt);
    }

    @Override
    public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        if (oldValue == null && newValue == null) {
            return;
        }
        super.firePropertyChange(propertyName, oldValue, newValue);
    }
}
