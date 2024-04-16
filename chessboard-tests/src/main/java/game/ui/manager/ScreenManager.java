package game.ui.manager;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import game.ui.segment.general.ResourceSupplier;
import game.ui.segment.general.UiSegment;

public class ScreenManager {

	private static final Logger LOG = LoggerFactory.getLogger(ScreenManager.class);

	private Terminal terminal;
	private Screen screen;

	public void closeResources() {
		closeCurrentScreen();
		closeTerminal();
	}

	public void openResources() throws IOException {
		createTerminal();
		createScreen();
	}

	private void createTerminal() throws IOException {
		closeTerminal();
		DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
		terminalFactory.setTerminalEmulatorTitle(ResourceSupplier.getLabel("program.title"));
		terminalFactory.setForceTextTerminal(false);
		terminalFactory.setPreferTerminalEmulator(true);
		terminal = terminalFactory.setInitialTerminalSize(new TerminalSize(100, 40)).createTerminal();
	}

	private void createScreen() throws IOException {
		closeCurrentScreen();

		if (terminal == null) {
			throw new IllegalArgumentException("Terminal is null");
		}

		screen = new TerminalScreen(terminal);
		screen.startScreen();
		screen.setCursorPosition(null);
		screen.doResizeIfNecessary();
	}

	public void clearScreen() {
		if (screen != null) {
			screen.clear();
		}
	}

	public void refreshScreen() throws IOException {
		if (screen != null) {
			screen.refresh();
		}
	}

	public void drawSegment(UiSegment segment) {
		if (screen != null) {
			segment.draw(screen.newTextGraphics());
		}
	}

	public KeyStroke readInput() throws IOException {
		return screen.readInput();
	}

	private void closeCurrentScreen() {
		if (screen != null) {
			try {
				screen.stopScreen();
				screen.close();
			} catch (IOException ex) {
				LOG.error("Can't close screen", ex);
			} finally {
				screen = null;
			}
		}
	}

	private void closeTerminal() {
		if (terminal != null) {
			try {
				terminal.close();
			} catch (Exception ex) {
				LOG.error("Can't close terminal", ex);
			} finally {
				terminal = null;
			}
		}
	}
}
