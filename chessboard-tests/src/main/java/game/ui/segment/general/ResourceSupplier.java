package game.ui.segment.general;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class ResourceSupplier {

	private static final String RESOURCE_FILENAME = "labels";
	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(RESOURCE_FILENAME);
	
	private ResourceSupplier() {
	}
	
	public static String getLabel(String labelName, Object... params) {
		String resource = getLabel(labelName);
		
		return MessageFormat.format(resource, params);
	}
	
	public static String getLabel(String labelName) {
		return RESOURCES.getString(labelName);
	}
	
}
