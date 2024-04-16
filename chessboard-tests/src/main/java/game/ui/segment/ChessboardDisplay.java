package game.ui.segment;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import game.chessboard.Field;
import game.coordinates.Coordinate;
import game.figures.Figure;
import game.ui.commands.CommandExecutor;
import game.ui.commands.CommandName;
import game.ui.input.InputConsumer;
import game.ui.segment.general.SegmentName;
import game.ui.segment.general.UiSegment;
import game.util.StyleUtil;

public class ChessboardDisplay implements UiSegment, InputConsumer {

	private static final int CELL_WIDTH = 7;
	private static final int CELL_HEIGHT = 3;
	private static final int BASE_SPACE_FROM_LEFT_EDGE = 20;
	private static final int BASE_SPACE_FROM_UPPER_EDGE = 3;
	private static final TextColor.ANSI SELECTED_FIELD_COLOR = TextColor.ANSI.BLUE_BRIGHT;
	
	private final CommandExecutor commandExecutor;

	private Map<TerminalPosition, String> staticOptions;
	private TerminalPosition selectedStaticOption;

	EnumMap<Coordinate, Field> boardState = new EnumMap<>(Coordinate.class);
	private Coordinate currentSelectedCoord;
	
	public ChessboardDisplay(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
		currentSelectedCoord = Coordinate.A1;
		initializeStaticOptions();
	}
	
	@Override
	public boolean handleInput(KeyStroke keyStroke) {
		KeyType key = keyStroke.getKeyType();
		switch (key) {
		case KeyType.ArrowUp, KeyType.ArrowDown, KeyType.ArrowLeft, KeyType.ArrowRight:
			switchActiveSegment(key);
			return true;
		case KeyType.Enter:
			executeSelectedOption();
			return true;
		case KeyType.Backspace:
			handleBackspace();
			return true;
		case KeyType.Escape:
			goBack();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void draw(TextGraphics graphics) {
		updateChessboardState();
		boardState = getChessboard();
		drawChessboardCoordinates(graphics);
		
		boardState.forEach((coord, field) -> {
			int x = BASE_SPACE_FROM_LEFT_EDGE + ((coord.getX() - 1) * CELL_WIDTH);
			int y = BASE_SPACE_FROM_UPPER_EDGE + ((coord.getY() - 1) * CELL_HEIGHT);
			drawField(graphics, x, y, field, coord.equals(currentSelectedCoord));
		});

		drawStaticOptions(graphics);
	}
	
	@Override
	public SegmentName getCurrentSegmentName() {
		return SegmentName.CHESSBOARD_DISPLAY;
	}
	
	@Override
	public void resetResources() {
		currentSelectedCoord = Coordinate.A1;
	}

	private void drawStaticOptions(TextGraphics graphics) {
		StyleUtil.resetColor(graphics);
		staticOptions.forEach((pos, option) -> {
			StyleUtil.changeHighlight(graphics, pos.equals(selectedStaticOption));
			graphics.putString(pos, option);
		});

	}

	private void initializeStaticOptions() {
		staticOptions = new LinkedHashMap<>();
		int startY = BASE_SPACE_FROM_UPPER_EDGE + 8 * CELL_HEIGHT + 2;
		staticOptions.put(new TerminalPosition(BASE_SPACE_FROM_LEFT_EDGE, startY), "Save Chessboard");
		staticOptions.put(new TerminalPosition(BASE_SPACE_FROM_LEFT_EDGE, startY + 2), "Go Back to Main Menu");
		selectedStaticOption = staticOptions.keySet().iterator().next();
	}

	private void drawChessboardCoordinates(TextGraphics graphics) {
		for (int i = 0; i < 8; i++) {
	        int x = BASE_SPACE_FROM_LEFT_EDGE + i * CELL_WIDTH + 3;
	        int y = BASE_SPACE_FROM_UPPER_EDGE - 1;
	        graphics.putString(x, y, String.valueOf((char)('A' + i)));
	    }
	    
	  for (int i = 0; i < 8; i++) {
	        int x = BASE_SPACE_FROM_LEFT_EDGE - 2;
	        int y = BASE_SPACE_FROM_UPPER_EDGE + i * CELL_HEIGHT + 1;
	        graphics.putString(x, y, Integer.toString(i + 1));
	    }
	}
	
	private void drawField(TextGraphics graphics, int x, int y, Field field, boolean isSelected) {
	    if(isSelected) {
			StyleUtil.changeBackgroundColor(graphics, SELECTED_FIELD_COLOR);
	    } else {
			changeBackgroundColor(graphics, field.isWhite());
	    }
	    
	    drawField(graphics, x, y);
		drawFigure(graphics, field.getFigure(), x, y, isSelected, field.isWhite());
	}
	
	private void drawFigure(TextGraphics graphics, Figure figure, int x, int y, boolean isSelected,
			boolean isFieldWhite) {
		if(Objects.nonNull(figure)) {
			TextColor.ANSI foregroundColor;

			if (isSelected) {
				foregroundColor = TextColor.ANSI.WHITE;
				graphics.enableModifiers(SGR.BOLD);
			} else {
				foregroundColor = isFieldWhite ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE_BRIGHT;
			}

			StyleUtil.changeForegroundColor(graphics, foregroundColor);
	    	graphics.putString(x + 3, y + 1, figure.getFigureSymbol());
			graphics.disableModifiers(SGR.BOLD);
	    }
	}
	
	private void drawField(TextGraphics graphics, int x, int y) {
		graphics.putString(x, y, "       ");
		graphics.putString(x, y + 1, "       ");
		graphics.putString(x, y + 2, "       ");
	}
	
	private void changeBackgroundColor(TextGraphics graphics, boolean isWhite) {
		TextColor.ANSI textColor = isWhite ? TextColor.ANSI.WHITE : TextColor.ANSI.BLACK_BRIGHT;
		StyleUtil.changeBackgroundColor(graphics, textColor);
	}
	
	private void switchActiveSegment(KeyType key) {
		int dx = 0;
		int dy = 0;
		
		switch(key) {
			case ArrowUp:
				dy = -1;
				break;
			case ArrowDown:
				dy = 1;
				break;
			case ArrowLeft:
				dx = -1;
				break;
			case ArrowRight:
				dx = 1;
				break;
			default:
				break;
		}
		
		Coordinate newCoord = calculateNewCoordinate(dx, dy);
		
		if(Objects.nonNull(newCoord)) {
			currentSelectedCoord = newCoord;
		}
	}
	
	private Coordinate calculateNewCoordinate(int dx, int dy) {
		int newX = currentSelectedCoord.getX() + dx;
	    int newY = currentSelectedCoord.getY() + dy;
	    return Coordinate.isValidCoordinate(newX, newY) ? Coordinate.fromXY(newX, newY) : null;
	}

	private void handleBackspace() {
		if (Objects.nonNull(currentSelectedCoord)) {
			switchToStaticOptions();
		} else {
			goBack();
		}
	}

	private void switchToStaticOptions() {
	}

	private void executeSelectedOption() {
		if(Objects.isNull(boardState.get(currentSelectedCoord).getFigure())) {
			putFigureOnSelectedField();
		} else {
			takeFigureFromSelectedField();
		}
	}
	
	private void updateChessboardState() {
		commandExecutor.executeCommand(CommandName.UPDATE_CHESSBOARD_STATE, this);
	}
	
	private void putFigureOnSelectedField() {
		commandExecutor.executeCommand(CommandName.PUT_FIGURE, this, currentSelectedCoord);
	}
	
	private void takeFigureFromSelectedField() {
		commandExecutor.executeCommand(CommandName.TAKE_FIGURE, this, currentSelectedCoord);
	}
	
	private EnumMap<Coordinate, Field> getChessboard() {
		return commandExecutor.executeCommand(CommandName.GET_CHESSBOARD, this);
	}
	
	private void goBack() {
		commandExecutor.executeCommand(CommandName.GO_BACK, this);
	}
}
