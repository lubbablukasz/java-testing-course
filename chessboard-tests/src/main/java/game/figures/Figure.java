package game.figures;

import java.io.Serializable;
import java.util.List;

import game.coordinates.Coordinate;

public interface Figure extends Serializable {
	
	/**
	 * Used to verify if movement to chosen destination is available
	 * and follows rules for chosen figure 
	 */
	public boolean isMovementRuleFollowed(Coordinate destination);
	
	public List<Coordinate> getFieldsInMovementRange();
	
	public Coordinate getStartingPosition();
	
	public Coordinate getCurrentPosition();
	
	public void setCurrentPosition(Coordinate coordinate);
}
