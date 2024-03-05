package game;

import java.util.List;

import game.chessboard.Chessboard;
import game.figures.Figure;

public class GameEngine {
	
	private final Chessboard chessboard;
	private final List<Figure> figures;
	
	public GameEngine(List<Figure> figures) {
		this.chessboard = new Chessboard();
		this.figures = figures;
	}
	
	
}
