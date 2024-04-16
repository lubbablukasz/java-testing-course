package game.ui.segment.general;

import java.lang.reflect.Constructor;
import java.util.Deque;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.ui.commands.CommandExecutor;

public class UiMediator {

	private static final Logger LOG = LoggerFactory.getLogger(UiMediator.class);
	
	private Map<SegmentName, UiSegment> segments = new EnumMap<>(SegmentName.class);
	private Deque<SegmentName> previousSegmentNames = new LinkedList<>();
	private UiSegment activeSegment;

	public void switchToSegment(SegmentName name) {
		if(activeSegment != null) {
			previousSegmentNames.push(activeSegment.getCurrentSegmentName());
		}
		activeSegment = segments.get(name);
	}

	public SegmentName getPreviousSegment() {
		if (!previousSegmentNames.isEmpty()) {
			return previousSegmentNames.pop();
		}
		return activeSegment.getCurrentSegmentName();
	}

	public UiSegment getActiveSegment() {
		return activeSegment;
	}

	public void registerAllSegments(CommandExecutor commandExecutor) {
		Reflections reflections = new Reflections("game.ui.segment");
		Set<Class<? extends UiSegment>> segmentClasses = reflections.getSubTypesOf(UiSegment.class);

        segmentClasses.forEach(segmentClazz -> registerSegment(segmentClazz, commandExecutor));
	}
	
	private void registerSegment(Class<? extends UiSegment> uiSegment, CommandExecutor commandExecutor) {
		try {
			Constructor<? extends UiSegment> constructor = uiSegment.getConstructor(CommandExecutor.class);
			UiSegment segmentInstance = constructor.newInstance(commandExecutor);
			segments.put(segmentInstance.getCurrentSegmentName(), segmentInstance);
		} catch (ReflectiveOperationException ex) {
			LOG.error("Error occured during UI segment registration: {}", uiSegment, ex);
		}
	}

}
