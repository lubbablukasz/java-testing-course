package game.ui.segment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import game.ui.commands.CommandExecutor;
import game.ui.commands.CommandName;
import game.ui.input.InputConsumer;
import game.ui.segment.general.ResourceSupplier;
import game.ui.segment.general.SegmentName;
import game.ui.segment.general.UiSegment;
import game.util.StyleUtil;

public class MainMenu implements UiSegment, InputConsumer {

	private final CommandExecutor commandExecutor;
	private Map<TerminalPosition, String> options;
	private int selectedOption;

	public MainMenu(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
		initializeOptions();
	}

	@Override
	public boolean handleInput(KeyStroke keyStroke) {
		KeyType key = keyStroke.getKeyType();
		switch (key) {
			case KeyType.ArrowUp, KeyType.ArrowDown:
				switchActiveSegment(key);
				return true;
			case KeyType.Enter:
				executeSelectedCommand();
				return true;
			default:
				return false;
		}
	}

	@Override
	public void draw(TextGraphics graphics) {
		graphics.putString(40, 12, ResourceSupplier.getLabel("menu.title"), SGR.BOLD);
		TerminalPosition selectedPosition = getSelectedPosition();
		options.keySet().forEach(pos -> {
			StyleUtil.changeHighlight(graphics, selectedPosition.equals(pos));
			graphics.putString(pos, options.get(pos));
		});
	}

	@Override
	public SegmentName getCurrentSegmentName() {
		return SegmentName.MAIN_MENU;
	}

	private TerminalPosition getSelectedPosition() {
		return new ArrayList<>(options.keySet()).get(selectedOption);
	}

	private void initializeOptions() {
		options = new LinkedHashMap<>();
		options.put(new TerminalPosition(38, 15), ResourceSupplier.getLabel("menu.create"));
		options.put(new TerminalPosition(36, 17), ResourceSupplier.getLabel("menu.load"));
		options.put(new TerminalPosition(47, 19), ResourceSupplier.getLabel("menu.exit"));
		selectedOption = 0;
	}

	private void switchActiveSegment(KeyType key) {
		int numButtons = options.size();
        selectedOption = (key == KeyType.ArrowUp) ? (selectedOption + numButtons - 1) % numButtons : (selectedOption + 1) % numButtons;
	}

	private void executeSelectedCommand() {
		switch (selectedOption) {
			case 0:
				commandExecutor.executeCommand(CommandName.NEW_CHESSBOARD, this);
				break;
			case 1:
				commandExecutor.executeCommand(CommandName.FILE_LOADER, this);
				break;
			case 2:
				commandExecutor.executeCommand(CommandName.EXIT, this);
				break;
		}
	}
}
