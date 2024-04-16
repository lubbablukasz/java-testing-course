package game.ui.input;

import com.googlecode.lanterna.input.KeyStroke;

public interface InputConsumer {

	boolean handleInput(KeyStroke keyStroke);
}
