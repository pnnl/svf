package gov.pnnl.svf.support;

import gov.pnnl.svf.camera.Camera;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.commons.math.geometry.Vector3D;

/**
 * This transform support object is for utilizing a transform support object to
 * push the translation to the camera as the location.
 *
 * @author Arthur Bleeker
 */
public class CameraTransformSupport extends TransformSupport {

    private final LocationListener locationListener = new LocationListener(this);
    private final TranslationListener translationListener;

    /**
     * Constructor kept private to prevent 'this' reference from escaping during
     * object construction.
     *
     * @param camera The camera that owns this transform support object.
     */
    protected CameraTransformSupport(final Camera camera) {
        super(camera);
        // set the initial translation
        setTranslation(camera.getLocation());
        // attach listeners
        translationListener = new TranslationListener(camera);
        getPropertyChangeSupport().addPropertyChangeListener(TransformSupport.TRANSLATION, translationListener);
        camera.getPropertyChangeSupport().addPropertyChangeListener(Camera.LOCATION, locationListener);
    }

    /**
     * Constructor
     *
     * @param camera The camera that owns this transform support object.
     *
     * @return a reference to the support object
     */
    public static CameraTransformSupport newInstance(final Camera camera) {
        final CameraTransformSupport instance = new CameraTransformSupport(camera);
        camera.add(instance);
        return instance;
    }

    @Override
    public void dispose() {
        super.dispose();
        getActor().getPropertyChangeSupport().removePropertyChangeListener(Camera.LOCATION, locationListener);
    }

    /**
     * Listener used to update the camera location property.
     */
    protected static class TranslationListener implements PropertyChangeListener {

        private final Camera camera;

        protected TranslationListener(final Camera camera) {
            this.camera = camera;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            camera.setLocation((Vector3D) evt.getNewValue());
        }
    }

    /**
     * Listener used to update the support translation property.
     */
    protected static class LocationListener implements PropertyChangeListener {

        private final TransformSupport instance;

        protected LocationListener(final TransformSupport instance) {
            this.instance = instance;
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            instance.setTranslation((Vector3D) evt.getNewValue());
        }
    }
}
