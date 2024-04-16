package game.figures;

import java.util.List;

import game.coordinates.Coordinate;

public class KingFigure implements Figure {

	private static final String FIGURE_SYMBOL = "K";
	
	private final Coordinate startingPosition;

	private Coordinate currentPosition;

	public KingFigure(Coordinate startingPosition) {
		this.startingPosition = startingPosition;
		this.currentPosition = startingPosition;
	}

	@Override
	public boolean isMovementRuleFollowed(Coordinate destination) {
		int dx = Math.abs(destination.getX() - getCurrentPosition().getX());
		int dy = Math.abs(destination.getY() - getCurrentPosition().getY());
		return dx <= 1 && dy <= 1;
	}

	@Override
	public List<Coordinate> getFieldsInMovementRange() {
		return Coordinate.getAllCoordinates().stream().filter(coord -> coord.distanceTo(this.getCurrentPosition()) == 1)
				.toList();
	}

	@Override
	public Coordinate getStartingPosition() {
		return startingPosition;
	}

	@Override
	public Coordinate getCurrentPosition() {
		return currentPosition;
	}

	@Override
	public void setCurrentPosition(Coordinate coordinate) {
		this.currentPosition = coordinate;
	}

	@Override
	public String getFigureSymbol() {
		return FIGURE_SYMBOL;
	}
}
