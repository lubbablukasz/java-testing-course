package game.ui.segment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

public class FileLoader implements UiSegment, InputConsumer {

	private static final int MAX_FILES_PER_PAGE = 5;

	private final CommandExecutor commandExecutor;
	private Map<TerminalPosition, String> options;
	private int activePage = 0;
	private int selectedOption = 0;
	private List<String> fileNames;

	public FileLoader(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
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
		case KeyType.Backspace:
			goBack();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void draw(TextGraphics graphics) {
		graphics.putString(30, 8, ResourceSupplier.getLabel("fileloader.title"));
		initializeOptions();

		if (fileNames.isEmpty()) {
			graphics.putString(35, 16, ResourceSupplier.getLabel("fileloader.emptyfilelist"), SGR.BOLD);
		} else {
			graphics.putString(43, 24, printPageCount());
		}

		TerminalPosition selectedPosition = getSelectedPosition();
		options.keySet().forEach(pos -> {
			StyleUtil.changeHighlight(graphics, selectedPosition.equals(pos));
			graphics.putString(pos, options.get(pos));
		});
	}

	@Override
	public SegmentName getCurrentSegmentName() {
		return SegmentName.FILE_LOADER;
	}

	@Override
	public void resetResources() {
		activePage = 0;
		selectedOption = 0;
	}

	private String printPageCount() {
		return ResourceSupplier.getLabel("fileloader.pagecount", activePage + 1, getPagesTotal());
	}

	private int getPagesTotal() {
		return (fileNames.size() + MAX_FILES_PER_PAGE - 1) / MAX_FILES_PER_PAGE;
	}
	
	private void printFileNames() {
		int startingRow = 12;
		int startIndex = activePage * MAX_FILES_PER_PAGE;
		int endIndex = Math.min(startIndex + MAX_FILES_PER_PAGE, fileNames.size());

		for (int i = startIndex; i < endIndex; i++) {
			options.put(new TerminalPosition(38, startingRow), fileNames.get(i));
			startingRow += 2;
		}
	}

	private TerminalPosition getSelectedPosition() {
		return new ArrayList<>(options.keySet()).get(selectedOption);
	}

	private void initializeOptions() {
		int shift = 0;
		options = new LinkedHashMap<>();
		fileNames = getAvaliableFileNames();

		if (!fileNames.isEmpty()) {
			printFileNames();
			initializePageOptions();
			shift = 6;
		}

		options.put(new TerminalPosition(38, 26 + shift), ResourceSupplier.getLabel("fileloader.create"));
		options.put(new TerminalPosition(38, 28 + shift), ResourceSupplier.getLabel("fileloader.goback"));
	}

	private void initializePageOptions() {
		options.put(new TerminalPosition(38, 26), ResourceSupplier.getLabel("fileloader.nextpage"));
		options.put(new TerminalPosition(38, 28), ResourceSupplier.getLabel("fileloader.previouspage"));
	}

	private void switchActiveSegment(KeyType key) {
		int numButtons = options.size();
		selectedOption = (key == KeyType.ArrowUp) ? (selectedOption + numButtons - 1) % numButtons
				: (selectedOption + 1) % numButtons;
	}

	private void executeSelectedCommand() {
		int totalFiles = fileNames.size();
	    int startIndex = activePage * MAX_FILES_PER_PAGE;
	    int endIndex = Math.min(startIndex + MAX_FILES_PER_PAGE, totalFiles);
	    int numberOfFiles = endIndex - startIndex;

	    if (selectedOption < numberOfFiles) {
	        loadChessboardFromFile(fileNames.get(startIndex + selectedOption));
	        return;
	    }

	    int staticOptionIndex = selectedOption - numberOfFiles;

	    if (fileNames.isEmpty()) {
	        switch (staticOptionIndex) {
	            case 0:
	                createNewChessboard();
	                break;
	            case 1:
	                goBack();
	                break;
	        }
	    } else {
	        switch (staticOptionIndex) {
	            case 0:
	                switchPage(false);
	                break;
	            case 1:
	                switchPage(true);
	                break;
	            case 2:
	                createNewChessboard();
	                break;
	            case 3:
	                goBack();
	                break;
	        }
	    }
	}

	private void switchPage(boolean forward) {
		int maxPage = (fileNames.size() + 4) / 5 - 1;
		if (forward) {
			activePage = activePage < maxPage ? activePage++ : 0;
		} else {
			activePage = activePage > 0 ? activePage-- : maxPage;
		}
	}

	private void createNewChessboard() {
		commandExecutor.executeCommand(CommandName.NEW_CHESSBOARD, this);
	}

	private void goBack() {
		commandExecutor.executeCommand(CommandName.GO_BACK, this, SegmentName.MAIN_MENU);
	}

	private List<String> getAvaliableFileNames() {
		return commandExecutor.executeCommand(CommandName.GET_FILE_NAMES, this);
	}

	private void loadChessboardFromFile(String fileName) {
		commandExecutor.executeCommand(CommandName.LOAD_CHESSBOARD_FROM_FILE, this, fileName);
	}
}
