package gov.pnnl.svf.input;

import gov.pnnl.svf.util.FxEventHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * This class keeps track of key state to help determine if a key is down or up.
 * Initial key states will be set to UP when this class is instantiated
 * regardless of actual key state.
 *
 * @author Amelia Bleeker
 *
 */
public class FxInputKeyAdapter implements FxEventHandler<InputEvent> {

    private static final Set<EventType<InputEvent>> EVENT_TYPES = Collections.unmodifiableSet(Collections.singleton(InputEvent.ANY));
    private final Map<Integer, KeyState> keyStates = new HashMap<>();

    /**
     * Constructor
     */
    public FxInputKeyAdapter() {
        super();
    }

    @Override
    public Set<EventType<InputEvent>> getEventTypes() {
        return EVENT_TYPES;
    }

    /**
     * @param keyCode The KeyEvent code for the key.
     *
     * @return the current key state, will not be null
     */
    public KeyState getKeyState(final int keyCode) {
        synchronized (keyStates) {
            final KeyState state = keyStates.get(keyCode);
            if (state == null) {
                keyStates.put(keyCode, KeyState.UP);
                return KeyState.UP;
            }
            return state;
        }
    }

    @Override
    public void handle(final InputEvent event) {
        // note: instanceof not necessary but included for static code analysis
        if (event instanceof KeyEvent && Objects.equals(event.getEventType(), KeyEvent.KEY_PRESSED)) {
            // key pressed
            synchronized (keyStates) {
                keyStates.put(Integer.valueOf(((KeyEvent) event).getCharacter().charAt(0)), KeyState.DOWN);
            }
        } else if (event instanceof KeyEvent && Objects.equals(event.getEventType(), KeyEvent.KEY_RELEASED)) {
            // key released
            synchronized (keyStates) {
                keyStates.put(Integer.valueOf(((KeyEvent) event).getCharacter().charAt(0)), KeyState.UP);
            }
        } else if (Objects.equals(event.getEventType(), MouseEvent.MOUSE_EXITED)) {
            // lost focus
            synchronized (keyStates) {
                for (final Map.Entry<Integer, KeyState> entry : keyStates.entrySet()) {
                    entry.setValue(KeyState.UP);
                }
            }
        }
    }
}
