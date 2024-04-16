package game.chessboard;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import game.coordinates.Coordinate;
import game.figures.Figure;

public class ChessboardService {

	private static final String EXCEPTION_MSG = "Chessboard is null";
	
	private Chessboard chessboard;

	public void createEmptyChessboard() {
		chessboard = new Chessboard();
	}

	public void createChessboardWithFigures(List<Figure> figures) {
		chessboard = new Chessboard(figures);
	}
	
	public EnumMap<Coordinate, Field> getChessboardCurrentState() {
		if(Objects.isNull(chessboard)) {
			throw new RuntimeException(EXCEPTION_MSG);
		}
		
		return chessboard.getChessboardCurrentState(); 
	}
	
	public void putFigureOnSelectedField(Coordinate selectedField) {
		chessboard.putKingFigureOnField(selectedField);
	}
	
	public void takeFigureFromSelectedField(Coordinate selectedField) {
		chessboard.takeFigureIfPossible(selectedField);
	}
	
	public void updateFieldStates() {
		chessboard.updateFieldStates();
	}
}
