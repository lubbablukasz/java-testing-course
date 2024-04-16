package game.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

	private static final String PATTERN = "yyyy-MM-dd_HH-mm-ss";
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);
	
	private DateTimeUtil() {
	}
	
	public static String getActualDateTimeString() {
		return LocalDateTime.now().format(FORMATTER);
	}
}
