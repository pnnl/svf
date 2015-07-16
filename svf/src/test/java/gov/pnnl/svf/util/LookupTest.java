package gov.pnnl.svf.util;

import gov.pnnl.svf.scene.ProxyGLCanvas;
import gov.pnnl.svf.scene.ProxyScene;
import gov.pnnl.svf.scene.Scene;
import gov.pnnl.svf.scene.SceneBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class LookupTest {

    public LookupTest() {
    }

    /**
     * Test of getLookup method, of class Lookup.
     */
    @Test
    public void testGetLookup() {
        // verify that global lookup is empty
        Assert.assertNotNull(Lookup.getLookup());
        Assert.assertNull(Lookup.getLookup().lookup(Scene.class));
        // create proxy scene
        final SceneBuilder sceneBuilder = ConfigUtil.configure();
        final ProxyGLCanvas component = new ProxyGLCanvas();
        final Scene scene = ProxyScene.newInstance(component, sceneBuilder);
        // check that started scene is present in the global lookup
        Assert.assertNotNull(Lookup.getLookup().lookup(Scene.class));
        Assert.assertTrue(Lookup.getLookup().lookup(Scene.class) == scene);
        // dispose the scene
        scene.dispose();
        // verify scene was cleaned from global lookup
        Assert.assertNull(Lookup.getLookup().lookup(Scene.class));
    }

}
