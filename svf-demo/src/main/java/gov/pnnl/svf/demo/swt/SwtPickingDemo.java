package gov.pnnl.svf.demo.swt;

import com.jogamp.opengl.swt.GLCanvas;
import gov.pnnl.svf.demo.Demo;
import gov.pnnl.svf.demo.PickingDemoLoader;
import gov.pnnl.svf.scene.SceneBuilder;
import gov.pnnl.svf.swt.scene.SwtScene;
import java.lang.reflect.Constructor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Amelia Bleeker
 */
public class SwtPickingDemo extends AbstractSwtDemo {

    /**
     * This main method must be copied into the final class for reflection to
     * work correctly.
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        boolean debug = false;
        for (final String string : args) {
            if ("debug".equals(string)) {
                debug = true;
            }
        }
        final Class<?> current = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
        final Constructor<?> constructor = current.getConstructor((Class<?>[]) null);
        final Demo<?, ?> demo = (Demo<?, ?>) constructor.newInstance((Object[]) null);
        demo.runDemo(debug);
    }

    @Override
    public SwtScene createScene(final Shell window, final SceneBuilder builder) {
        final Composite composite = new Composite(window, SWT.NONE);
        composite.setBackgroundMode(SWT.INHERIT_FORCE);
        final GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        window.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new FillLayout());
        // create a test scene
        final GLCanvas component = new GLCanvas(composite, SWT.NONE, builder.getGLCapabilities(), null);
        final SwtScene scene = new SwtScene(component, builder) {
            @Override
            public void load() {
                final PickingDemoLoader loader = new PickingDemoLoader();
                loader.load(this);
            }
        };
        return scene;
    }
}
