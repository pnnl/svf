package gov.pnnl.svf.swt.input;

import gov.pnnl.svf.input.KeyState;
import junit.framework.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 *
 * @author Arthur Bleeker
 */
public class SwtInputKeyAdapterTest {

    public SwtInputKeyAdapterTest() {
    }

    /**
     * Test of getKeyState method, of class SwtInputKeyAdapter.
     */
    @Test
    public void testGetKeyState() {
        final SwtInputKeyAdapter instance = new SwtInputKeyAdapter();

        Assert.assertEquals(KeyState.UP, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('B'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('+'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('-'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState(123343243));
    }

    /**
     * Test of keyPressed method, of class SwtInputKeyAdapter.
     */
    @Test
    public void testKeyPressed() {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        final SwtInputKeyAdapter instance = new SwtInputKeyAdapter();
        final Event event = new Event();
        event.widget = new Button(shell, SWT.NONE);
        event.keyCode = 'a';
        event.character = 'a';
        final KeyEvent keyEvent = new KeyEvent(event);
        shell.getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                instance.keyPressed(keyEvent);
            }
        });

        Assert.assertEquals(KeyState.DOWN, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('A'));

        shell.dispose();
        display.dispose();
    }

    /**
     * Test of keyReleased method, of class SwtInputKeyAdapter.
     */
    @Test
    public void testKeyReleased() {
        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        final SwtInputKeyAdapter instance = new SwtInputKeyAdapter();
        final Event event = new Event();
        event.widget = new Button(shell, SWT.NONE);
        event.keyCode = 'a';
        event.character = 'a';
        final KeyEvent keyEvent = new KeyEvent(event);
        shell.getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                instance.keyPressed(keyEvent);
                instance.keyReleased(keyEvent);
            }
        });

        Assert.assertEquals(KeyState.UP, instance.getKeyState('a'));
        Assert.assertEquals(KeyState.UP, instance.getKeyState('A'));

        shell.dispose();
        display.dispose();
    }
}
