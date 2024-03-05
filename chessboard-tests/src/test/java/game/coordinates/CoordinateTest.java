package game.coordinates;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class CoordinateTest {

	@Test
	public void should_returnCorrectDistance() {
		// given
		Coordinate coordinate = Coordinate.E4;
		Coordinate targetWithOneFieldPath = Coordinate.D4;
		Coordinate targetWithTwoFieldsPath = Coordinate.C4;
		
		// then
		assertThat(coordinate.distanceTo(targetWithOneFieldPath)).isEqualTo(1);
		assertThat(coordinate.distanceTo(targetWithTwoFieldsPath)).isEqualTo(2);
	}
}
