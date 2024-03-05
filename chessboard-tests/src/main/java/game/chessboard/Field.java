package game.chessboard;

import java.io.Serializable;

import game.figures.Figure;

public class Field implements Serializable {

	private static final int NOT_ACTIVE_FIELD = 0;

	private Figure figure;
	private int fieldState;

	public Field() {
		this(null);
	}

	public Field(Figure figure) {
		this.setFigure(figure);
		this.setFieldNotActive();
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public int getFieldValue() {
		return fieldState;
	}

	public void setFieldNotActive() {
		this.fieldState = NOT_ACTIVE_FIELD;
	}

	public void incrementFieldState() {
		fieldState++;
	}
}
