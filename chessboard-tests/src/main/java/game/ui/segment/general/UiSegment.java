package game.ui.segment.general;

import com.googlecode.lanterna.graphics.TextGraphics;

public interface UiSegment {

	void draw(TextGraphics graphics);

	SegmentName getCurrentSegmentName();
	
	default void resetResources() {};
}
