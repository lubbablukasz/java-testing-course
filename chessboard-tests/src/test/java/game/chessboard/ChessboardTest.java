package game.chessboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import game.chessboard.exception.InitializationException;
import game.coordinates.Coordinate;
import game.figures.KingFigure;

public class ChessboardTest {

	@Test
	void should_initializeEmptyChessboard() {
		// given
		Chessboard chessboard;
		EnumMap<Coordinate, Field> boardRepresentation;
		Field expectedFieldWhite = new Field(true);
		Field expectedFieldBlack = new Field(false);

		// when
		chessboard = new Chessboard();

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(boardRepresentation).hasSize(64).containsOnlyKeys(Coordinate.getAllCoordinates());
		assertThat(boardRepresentation.values()).usingRecursiveFieldByFieldElementComparator()
				.containsOnlyElementsOf(List.of(expectedFieldWhite, expectedFieldBlack));
	}

	@Test
	void should_initializeChessboard_whenGivenFiguresAreEmpty() {
		// given
		Chessboard chessboard;
		EnumMap<Coordinate, Field> boardRepresentation;
		Field expectedFieldWhite = new Field(true);
		Field expectedFieldBlack = new Field(false);

		// when
		chessboard = new Chessboard(Collections.emptyList());

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(boardRepresentation).hasSize(64).containsOnlyKeys(Coordinate.getAllCoordinates());
		assertThat(boardRepresentation.values()).usingRecursiveFieldByFieldElementComparator()
				.containsOnlyElementsOf(List.of(expectedFieldWhite, expectedFieldBlack));
	}

	@Test
	void should_throwException_whenTwoFiguresInitialized_atTheSameField() {
		// given
		KingFigure firstFigure = new KingFigure(Coordinate.E1);
		KingFigure secondFigure = new KingFigure(Coordinate.E1);

		// then
		assertThatExceptionOfType(InitializationException.class)
				.isThrownBy(() -> new Chessboard(List.of(firstFigure, secondFigure)));
	}

	@Test
	void should_putKingFigureOnBoard_setUpInRightField() {
		// given
		Chessboard chessboard;
		EnumMap<Coordinate, Field> boardRepresentation;
		Coordinate startingPosition = Coordinate.B1;
		KingFigure kingFigure = new KingFigure(startingPosition);

		// when
		chessboard = new Chessboard(List.of(kingFigure));

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();

		assertThat(kingFigure.getStartingPosition()).isEqualTo(startingPosition);
		assertThat(kingFigure.getCurrentPosition()).isEqualTo(startingPosition);

		assertThat(boardRepresentation.get(startingPosition).getFigure()).usingRecursiveComparison()
				.isEqualTo(kingFigure);
		assertThat(boardRepresentation.entrySet()).filteredOn(entry -> !startingPosition.equals(entry.getKey()))
				.allSatisfy(entry -> {
					assertThat(entry.getValue().getFigure()).isNull();
				});
	}

	@Test
	void should_notMoveToChosenField_whenIllegalMove() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		Coordinate startingPosition = Coordinate.E1;
		Coordinate target = Coordinate.E3;
		Coordinate emptyFieldPosition = Coordinate.E2;
		KingFigure kingFigure = new KingFigure(startingPosition);
		Chessboard chessboard = new Chessboard(List.of(kingFigure));

		// target out of range for chosen figure
		// when
		chessboard.moveFigure(startingPosition, target);

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(boardRepresentation.get(startingPosition).getFigure()).usingRecursiveComparison()
				.isEqualTo(kingFigure);
		assertThat(boardRepresentation.get(target).getFigure()).isNull();
		assertThat(kingFigure.getCurrentPosition()).isEqualTo(startingPosition);

		// movement from empty field to taken (shouldn't replace figure with null)
		// when
		chessboard.moveFigure(emptyFieldPosition, startingPosition);

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(boardRepresentation.get(startingPosition).getFigure()).usingRecursiveComparison()
				.isEqualTo(kingFigure);
		assertThat(kingFigure.getCurrentPosition()).isEqualTo(startingPosition);

		// moving chosen figure to its current position doesn't require any test
		// as it does nothing in our case, but game logic prevents player from wasting
		// his move
	}

	@Test
	void should_moveToChosenField_whenLegalMove() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		Coordinate startingPosition = Coordinate.E1;
		Coordinate target = Coordinate.E2;
		KingFigure kingFigure = new KingFigure(startingPosition);
		Chessboard chessboard = new Chessboard(List.of(kingFigure));

		// when
		chessboard.moveFigure(startingPosition, target);

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(boardRepresentation.get(startingPosition).getFigure()).isNull();
		assertThat(boardRepresentation.get(target).getFigure()).usingRecursiveComparison().isEqualTo(kingFigure);
		assertThat(kingFigure.getCurrentPosition()).isEqualTo(target);
	}

	@Test
	void should_removeFigureFromBoard_whenTakenByAnotherFigure() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		Coordinate firstFigurePosition = Coordinate.E1;
		Coordinate secondFigurePosition = Coordinate.E2;
		KingFigure firstFigure = new KingFigure(firstFigurePosition);
		KingFigure secondFigure = new KingFigure(secondFigurePosition);
		Chessboard chessboard = new Chessboard(List.of(firstFigure, secondFigure));

		// when
		chessboard.moveFigure(firstFigurePosition, secondFigurePosition);

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();
		assertThat(firstFigure.getCurrentPosition()).isEqualTo(secondFigurePosition);
		assertThat(secondFigure.getCurrentPosition()).isNull(); // indicates that this figure was taken - has no current position assigned
		assertThat(boardRepresentation.get(firstFigurePosition).getFigure()).isNull();
		assertThat(boardRepresentation.get(secondFigurePosition).getFigure()).usingRecursiveComparison()
				.isEqualTo(firstFigure);
	}

	@Test
	void should_updateFieldStatesCorrectly_whenOneFigureInitialized_inTheCenter() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		KingFigure firstFigure = new KingFigure(Coordinate.E4);
		Chessboard chessboard = new Chessboard(List.of(firstFigure));

		// when
		chessboard.updateFieldStates();

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();

		assertThat(boardRepresentation.get(Coordinate.D5).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.E5).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.F5).getFieldValue()).isEqualTo(1);

		assertThat(boardRepresentation.get(Coordinate.D4).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.E4).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.F4).getFieldValue()).isEqualTo(1);

		assertThat(boardRepresentation.get(Coordinate.D3).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.E3).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.F3).getFieldValue()).isEqualTo(1);

	}

	@Test
	void should_updateFieldStatesCorrectly_whenOneFigureInitialized_inTheCorner() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		KingFigure firstFigure = new KingFigure(Coordinate.A1);
		Chessboard chessboard = new Chessboard(List.of(firstFigure));

		// when
		chessboard.updateFieldStates();

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();

		assertThat(boardRepresentation.get(Coordinate.A2).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.B1).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.B2).getFieldValue()).isEqualTo(1);
	}

	@Test
	void should_updateFieldStatesCorrectly_whenMultipleFiguresCrossRoads() {
		// given
		EnumMap<Coordinate, Field> boardRepresentation;
		KingFigure firstFigure = new KingFigure(Coordinate.E1);
		KingFigure secondFigure = new KingFigure(Coordinate.E2);
		KingFigure thirdFigure = new KingFigure(Coordinate.D1);
		Chessboard chessboard = new Chessboard(List.of(firstFigure, secondFigure, thirdFigure));

		// when
		chessboard.updateFieldStates();

		// then
		boardRepresentation = chessboard.getChessboardCurrentState();

		assertThat(boardRepresentation.get(Coordinate.C1).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.D1).getFieldValue()).isEqualTo(3);
		assertThat(boardRepresentation.get(Coordinate.E1).getFieldValue()).isEqualTo(3);
		assertThat(boardRepresentation.get(Coordinate.F1).getFieldValue()).isEqualTo(2);

		assertThat(boardRepresentation.get(Coordinate.C2).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.D2).getFieldValue()).isEqualTo(3);
		assertThat(boardRepresentation.get(Coordinate.E2).getFieldValue()).isEqualTo(3);
		assertThat(boardRepresentation.get(Coordinate.F2).getFieldValue()).isEqualTo(2);

		assertThat(boardRepresentation.get(Coordinate.D3).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.E3).getFieldValue()).isEqualTo(1);
		assertThat(boardRepresentation.get(Coordinate.F3).getFieldValue()).isEqualTo(1);
	}
}
