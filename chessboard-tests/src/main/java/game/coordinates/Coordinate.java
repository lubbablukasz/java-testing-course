package game.coordinates;

import java.util.List;

public enum Coordinate {
	// A
	A1, A2, A3, A4, A5, A6, A7, A8,

	// B
	B1, B2, B3, B4, B5, B6, B7, B8,

	// C
	C1, C2, C3, C4, C5, C6, C7, C8,

	// D
	D1, D2, D3, D4, D5, D6, D7, D8,

	// E
	E1, E2, E3, E4, E5, E6, E7, E8,

	// F
	F1, F2, F3, F4, F5, F6, F7, F8,

	// G
	G1, G2, G3, G4, G5, G6, G7, G8,

	// H
	H1, H2, H3, H4, H5, H6, H7, H8;

	public int getX() {
		return ordinal() % 8 + 1;
	}

	public int getY() {
		return ordinal() / 8 + 1;
	}

	public static boolean isValidCoordinate(Coordinate coordinate) {
		int x = coordinate.getX();
		int y = coordinate.getY();

		return x >= 1 && x <= 8 && y >= 1 && y <= 8;
	}

	/**
	 * Uses Chebyshev distance formula to calculate distance between two coordinates
	 */
	public int distanceTo(Coordinate other) {
		int dx = Math.abs(this.getX() - other.getX());
		int dy = Math.abs(this.getY() - other.getY());
		return Math.max(dx, dy);
	}

	public static List<Coordinate> getAllCoordinates() {
		return List.of(Coordinate.values());
	}
}