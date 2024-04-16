package game.util;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class StyleUtil {

	private StyleUtil() {
	}
	
	public static void changeHighlight(TextGraphics graphics, boolean isHighlighted) {
		if (isHighlighted) {
			graphics.setBackgroundColor(TextColor.ANSI.WHITE);
			graphics.setForegroundColor(TextColor.ANSI.BLACK);
		} else {
			graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
			graphics.setForegroundColor(TextColor.ANSI.WHITE);
		}
	}
	
	public static void resetColor(TextGraphics graphics) {
		graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
	    graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
	}
	
	public static void changeColor(TextGraphics graphics, TextColor.ANSI desiredColor) {
		graphics.setBackgroundColor(desiredColor);
	    graphics.setForegroundColor(desiredColor);
	}

	public static void changeBackgroundColor(TextGraphics graphics, TextColor.ANSI color) {
		graphics.setBackgroundColor(color);
	}

	public static void changeForegroundColor(TextGraphics graphics, TextColor.ANSI color) {
		graphics.setForegroundColor(color);
	}
}
