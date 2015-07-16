package gov.pnnl.svf.swt.input;

import gov.pnnl.svf.input.KeyState;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

/**
 * This class keeps track of key state to help determine if a key is down or up.
 * Initial key states will be set to UP when this class is instantiated
 * regardless of actual key state.
 *
 * @author Arthur Bleeker
 *
 */
public class SwtInputKeyAdapter implements KeyListener, FocusListener {

    private final Map<Integer, KeyState> keyStates = new HashMap<>();

    /**
     * Constructor
     */
    public SwtInputKeyAdapter() {
        super();
    }

    /**
     * @param keyCode The KeyEvent code for the key.
     *
     * @return the current key state, will not be null
     */
    public KeyState getKeyState(final int keyCode) {
        synchronized (keyStates) {
            final KeyState state = keyStates.get(Integer.valueOf(keyCode));
            if (state == null) {
                keyStates.put(keyCode, KeyState.UP);
                return KeyState.UP;
            }
            return state;
        }
    }

    /**
     *
     * @param evt
     */
    @Override
    public void keyPressed(final KeyEvent evt) {
        synchronized (keyStates) {
            keyStates.put(evt.keyCode, KeyState.DOWN);
        }
    }

    /**
     *
     * @param evt
     */
    @Override
    public void keyReleased(final KeyEvent evt) {
        synchronized (keyStates) {
            keyStates.put(evt.keyCode, KeyState.UP);
        }
    }

    @Override
    public void focusGained(final FocusEvent evt) {
        // no operation
    }

    @Override
    public void focusLost(final FocusEvent evt) {
        synchronized (keyStates) {
            for (final Map.Entry<Integer, KeyState> entry : keyStates.entrySet()) {
                entry.setValue(KeyState.UP);
            }
        }
    }
}
