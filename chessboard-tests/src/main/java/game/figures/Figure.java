package game.figures;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import game.coordinates.Coordinate;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = KingFigure.class, name = "king")
})
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
	
	public String getFigureSymbol();
}
