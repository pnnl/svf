package gov.pnnl.svf.awt.input;

import gov.pnnl.svf.input.KeyState;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of key state to help determine if a key is down or up.
 * Initial key states will be set to UP when this class is instantiated
 * regardless of actual key state.
 *
 * @author Arthur Bleeker
 *
 */
public class AwtInputKeyAdapter implements KeyListener, FocusListener {

    private final Map<Integer, KeyState> keyStates = new HashMap<>();

    /**
     * Constructor
     */
    public AwtInputKeyAdapter() {
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

    @Override
    public void keyPressed(final KeyEvent evt) {
        synchronized (keyStates) {
            keyStates.put(evt.getKeyCode(), KeyState.DOWN);
        }
    }

    @Override
    public void keyReleased(final KeyEvent evt) {
        synchronized (keyStates) {
            keyStates.put(evt.getKeyCode(), KeyState.UP);
        }
    }

    @Override
    public void keyTyped(final KeyEvent evt) {
        // not needed
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
