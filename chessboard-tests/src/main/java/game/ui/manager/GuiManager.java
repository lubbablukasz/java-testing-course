package game.ui.manager;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import game.chessboard.ChessboardService;
import game.chessboard.Field;
import game.coordinates.Coordinate;
import game.figures.Figure;
import game.files.FileService;
import game.ui.commands.Command;
import game.ui.commands.CommandName;
import game.ui.commands.CommandRegistry;
import game.ui.input.InputConsumer;
import game.ui.input.InputHandler;
import game.ui.segment.general.SegmentName;
import game.ui.segment.general.UiMediator;
import game.ui.segment.general.UiSegment;

public class GuiManager {

	private static final Logger LOG = LoggerFactory.getLogger(GuiManager.class);

	private final CommandRegistry commandRegistry;
	private final ScreenManager screenManager;
	private final InputHandler inputHandler;
	private final FileService fileService;
	private final ChessboardService chessboardService;
	private final UiMediator uiMediator;

	private boolean isRunning = true;

	public GuiManager() {
		commandRegistry = new CommandRegistry();
		screenManager = new ScreenManager();
		inputHandler = new InputHandler();
		fileService = new FileService();
		chessboardService = new ChessboardService();
		uiMediator = new UiMediator();
		initialize();
	}

	public void runGui() {
		try {
			while (isRunning) {
				KeyStroke keyStroke = screenManager.readInput();
				if (keyStroke != null) {
					handleEOFKeyStroke(keyStroke);
					processInput(keyStroke);
				}
			}
		} catch (Exception ex) {
			LOG.error("Error occurred during the main loop", ex);
		} finally {
			stopGui();
		}
	}

	private void handleEOFKeyStroke(KeyStroke keyStroke) {
		if (keyStroke.getKeyType() == KeyType.EOF) {
			isRunning = false;
		}
	}

	private void processInput(KeyStroke keyStroke) {
		boolean consumed = inputHandler.handleKeyStroke(keyStroke);
		if (consumed) {
			redrawScreen();
		}
	}

	private void initialize() {
		try {
			screenManager.openResources();
			uiMediator.registerAllSegments(commandRegistry);
			commandRegistry.scanAndRegisterCommands(this);
			switchToSegment(SegmentName.MAIN_MENU);
			redrawScreen();
		} catch (Exception ex) {
			LOG.error("Error while initializing GUI", ex);
		}
	}

	private void switchToSegment(SegmentName segmentName) {
		
		if(SegmentName.FILE_LOADER.equals(segmentName)) {
			fileService.checkDirectory();
		}
		
		uiMediator.switchToSegment(segmentName);
		UiSegment segment = uiMediator.getActiveSegment();
		segment.resetResources();
		if (segment instanceof InputConsumer consumer) {
			inputHandler.setCurrentConsumer(consumer);
		} else {
			inputHandler.setCurrentConsumer(null);
		}
	}

	private void redrawScreen() {
		try {
			screenManager.clearScreen();
			screenManager.drawSegment(uiMediator.getActiveSegment());
			screenManager.refreshScreen();
		} catch (IOException ex) {
			LOG.error("Error occured while drawing screen", ex);
		}
	}

	// Commands

	@Command(name = CommandName.EXIT)
	private void stopGui() {
		isRunning = false;
		screenManager.closeResources();
	}
	
	@Command(name = CommandName.GO_BACK)
	private void goBack(SegmentName segmentName) {
		if (Objects.nonNull(segmentName)) {
			switchToSegment(segmentName);
		} else {
			switchToSegment(uiMediator.getPreviousSegment());
		}
	}

	@Command(name = CommandName.NEW_CHESSBOARD, permissions = {SegmentName.MAIN_MENU, SegmentName.FILE_LOADER})
	private void createNewChessboard() {
		chessboardService.createEmptyChessboard();
		switchToSegment(SegmentName.CHESSBOARD_DISPLAY);
	}

	@Command(name = CommandName.FILE_LOADER, permissions = SegmentName.MAIN_MENU)
	private void switchToFileLoader() {
		switchToSegment(SegmentName.FILE_LOADER);
	}

	@Command(name = CommandName.GET_FILE_NAMES, permissions = SegmentName.FILE_LOADER)
	private List<String> getFileNames() {
		return fileService.getFileNames();
	}
	
	@Command(name = CommandName.LOAD_CHESSBOARD_FROM_FILE, permissions = SegmentName.FILE_LOADER)
	private void loadChessboardFromFile(String fileName) {
		List<Figure> figures = fileService.loadDataFromFile(fileName);
		chessboardService.createChessboardWithFigures(figures);
		switchToSegment(SegmentName.CHESSBOARD_DISPLAY);
	}
	
	@Command(name = CommandName.GET_CHESSBOARD, permissions = SegmentName.CHESSBOARD_DISPLAY)
	private EnumMap<Coordinate, Field> getChessboard() {
		return chessboardService.getChessboardCurrentState();
	}
	
	@Command(name = CommandName.PUT_FIGURE, permissions = SegmentName.CHESSBOARD_DISPLAY)
	private void putFigureOnSelectedField(Coordinate selectedField) {
		chessboardService.putFigureOnSelectedField(selectedField);
	}
	
	@Command(name = CommandName.TAKE_FIGURE, permissions = SegmentName.CHESSBOARD_DISPLAY)
	private void takeFigureFromSelectedField(Coordinate selectedField) {
		chessboardService.takeFigureFromSelectedField(selectedField);
	}
	
	@Command(name = CommandName.UPDATE_CHESSBOARD_STATE, permissions = SegmentName.CHESSBOARD_DISPLAY)
	private void updateChessboardState() {
		chessboardService.updateFieldStates();
	}
}