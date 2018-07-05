package gov.pnnl.svf.awt.input;

import gov.pnnl.svf.input.KeyState;
import java.awt.Label;
import java.awt.event.KeyEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Amelia Bleeker
 */
public class AwtInputKeyAdapterTest {

    public AwtInputKeyAdapterTest() {
    }

    /**
     * Test of getKeyState method, of class AwtInputKeyAdapter.
     */
    @Test
    public void testGetKeyState() {
        final AwtInputKeyAdapter instance = new AwtInputKeyAdapter();

        Assert.assertEquals(KeyState.UP, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('B'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('+'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('-'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState(123343243));
    }

    /**
     * Test of keyPressed method, of class AwtInputKeyAdapter.
     */
    @Test
    public void testKeyPressed() {
        final AwtInputKeyAdapter instance = new AwtInputKeyAdapter();
        instance.keyPressed(new KeyEvent(new Label(), 1, System.currentTimeMillis(), 0, 'a', 'a'));

        Assert.assertEquals(KeyState.DOWN, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('A'));
    }

    /**
     * Test of keyReleased method, of class AwtInputKeyAdapter.
     */
    @Test
    public void testKeyReleased() {
        final AwtInputKeyAdapter instance = new AwtInputKeyAdapter();
        instance.keyPressed(new KeyEvent(new Label(), 1, System.currentTimeMillis(), 0, 'a', 'a'));
        instance.keyReleased(new KeyEvent(new Label(), 1, System.currentTimeMillis(), 0, 'a', 'a'));

        Assert.assertEquals(KeyState.UP, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('A'));
    }

    /**
     * Test of keyTyped method, of class AwtInputKeyAdapter.
     */
    @Test
    public void testKeyTyped() {
        final AwtInputKeyAdapter instance = new AwtInputKeyAdapter();
        instance.keyTyped(new KeyEvent(new Label(), 1, System.currentTimeMillis(), 0, 'a', 'a'));

        Assert.assertEquals(KeyState.UP, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('A'));
    }
}
