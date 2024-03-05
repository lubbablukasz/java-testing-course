package game.chessboard;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

import game.chessboard.exception.InitializationException;
import game.coordinates.Coordinate;
import game.figures.Figure;

public class Chessboard {

	private EnumMap<Coordinate, Field> board = new EnumMap<>(Coordinate.class);

	public Chessboard() {
		initializeChessboard();
	}

	public Chessboard(List<Figure> figures) {
		initializeChessboard();
		setFiguresOnChessboard(figures);
	}

	/**
	 * Return a copy of entire board to only show current state and secure game data
	 * from uncontrolled modification
	 */
	public EnumMap<Coordinate, Field> getChessboardCurrentState() {
		return SerializationUtils.clone(board);
	}

	public void setFiguresOnChessboard(List<Figure> figures) {
		figures.forEach(figure -> {
			Coordinate coordinate = figure.getStartingPosition();
			if (board.containsKey(coordinate)) {
				if(Objects.nonNull(getFigureFromField(coordinate))) {
					throw new InitializationException("Cannot initialize chessboard, multiple figures were set for the same field");
				}
				board.get(coordinate).setFigure(figure);
			}
		});
	}

	public void updateFieldStates() {
		resetFieldStates();
		board.values().stream().filter(field -> Objects.nonNull(field.getFigure())).forEach(field -> {
			Figure figure = field.getFigure();
			updateFieldState(figure.getCurrentPosition(), figure.getFieldsInMovementRange());
		});
	}

	public void moveFigure(Coordinate from, Coordinate to) {
		if (isValidMove(from, to)) {
			takeFigureIfPossible(to);
			Figure chosenFigure = getAndDeleteFigureFromField(from);
			chosenFigure.setCurrentPosition(to);
			setFigureToField(chosenFigure, to);
		}
	}

	private void initializeChessboard() {
		if (board.isEmpty()) {
			Coordinate.getAllCoordinates().forEach(coordinate -> {
				board.put(coordinate, new Field());
			});
		}
	}

	private void updateFieldState(Coordinate currentPosition, List<Coordinate> availableFields) {
		board.get(currentPosition).incrementFieldState();
		availableFields.forEach(coord -> {
			Field field = board.get(coord);
			if (Objects.nonNull(field)) {
				field.incrementFieldState();
			}
		});
	}

	private void resetFieldStates() {
		board.values().forEach(Field::setFieldNotActive);
	}

	private boolean isValidMove(Coordinate from, Coordinate to) {
		return !isChosenFieldEmpty(from) && Coordinate.isValidCoordinate(to) && isMovementRuleFollowed(from, to)
				&& isChosenFieldNotCurrentPosition(from, to);
	}

	private boolean isChosenFieldNotCurrentPosition(Coordinate from, Coordinate to) {
		return !from.equals(to);
	}

	private boolean isMovementRuleFollowed(Coordinate from, Coordinate to) {
		return getFigureFromField(from).isMovementRuleFollowed(to);
	}

	private boolean isChosenFieldEmpty(Coordinate coordinate) {
		return Objects.isNull(getChosenField(coordinate).getFigure());
	}

	private Field getChosenField(Coordinate coordinate) {
		return board.get(coordinate);
	}

	private Figure getFigureFromField(Coordinate coordinate) {
		return getChosenField(coordinate).getFigure();
	}

	private void takeFigureIfPossible(Coordinate coordinate) {
		if (!isChosenFieldEmpty(coordinate)) {
			getFigureFromField(coordinate).setCurrentPosition(null);
		}
	}

	private void setFigureToField(Figure figure, Coordinate coordinate) {
		getChosenField(coordinate).setFigure(figure);
	}

	private Figure getAndDeleteFigureFromField(Coordinate coordinate) {
		Figure figure = getFigureFromField(coordinate);
		setFigureToField(null, coordinate);
		return figure;
	}
}
