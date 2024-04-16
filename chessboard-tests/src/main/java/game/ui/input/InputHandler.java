package game.ui.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class InputHandler {

	private static final Logger LOG = LoggerFactory.getLogger(InputHandler.class);

	private InputConsumer currentConsumer;

	public void setCurrentConsumer(InputConsumer currentConsumer) {
		this.currentConsumer = currentConsumer;
	}

	public boolean handleKeyStroke(KeyStroke keyStroke) {
		if (currentConsumer != null && keyStroke.getKeyType() != KeyType.EOF) {
			try {
				return currentConsumer.handleInput(keyStroke) ? true : handleUnconsumedInput(keyStroke);
			} catch (Exception ex) {
				handleInputError(ex, keyStroke);
			}
		}
		return false;
	}

	private boolean handleUnconsumedInput(KeyStroke keyStroke) {
		LOG.debug("Unconsumed input: {}", keyStroke);
		return false;
	}

	private void handleInputError(Exception ex, KeyStroke keyStroke) {
		LOG.warn("Error occured while consuming user input {}", keyStroke, ex);
	}
}
