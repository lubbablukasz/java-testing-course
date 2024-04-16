package game.ui.commands;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.ui.commands.exception.CommandNotPermittedException;
import game.ui.segment.general.SegmentName;
import game.ui.segment.general.UiSegment;

public class CommandRegistry implements CommandExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(CommandRegistry.class);

	private Map<CommandName, CommandAction<?, ?>> commands = new EnumMap<>(CommandName.class);
	private Map<CommandName, Set<SegmentName>> commandPermissions = new EnumMap<>(CommandName.class);

	@SuppressWarnings("unchecked")
	@Override
	public <R> R executeCommand(CommandName commandName, UiSegment requestingSegment) {
		SegmentName requestingSegmentName = requestingSegment.getCurrentSegmentName();
		if (checkPermissions(commandName, requestingSegmentName)) {
			CommandAction<Void, R> action = (CommandAction<Void, R>) commands.get(commandName);
			return executeCommand(action, null);
		}
		throw new CommandNotPermittedException(commandName, requestingSegmentName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> R executeCommand(CommandName commandName, UiSegment requestingSegment, T input) {
		SegmentName requestingSegmentName = requestingSegment.getCurrentSegmentName();
		if (checkPermissions(commandName, requestingSegmentName)) {
			CommandAction<T, R> action = (CommandAction<T, R>) commands.get(commandName);
			return executeCommand(action, input);
		}
		throw new CommandNotPermittedException(commandName, requestingSegmentName);
	}

	public void scanAndRegisterCommands(Object target) {
		Stream.of(target.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Command.class))
				.forEach(method -> {
					Command annotation = method.getAnnotation(Command.class);
					CommandName name = annotation.name();
					Set<SegmentName> permissions = getPermissions(annotation.permissions());
					CommandAction<?, ?> action = createCommandAction(target, method);
					registerCommand(name, action, permissions);
				});
	}

	private <T, R> R executeCommand(CommandAction<T, R> action, T input) {
		try {
			return action.execute(input);
		} catch (Exception e) {
			LOG.error("Error executing command: ", e);
		}
		return null;
	}
	
	private boolean checkPermissions(CommandName commandName, SegmentName segmentName) {
		Set<SegmentName> permissions = commandPermissions.get(commandName);
		return permissions.isEmpty() || permissions.contains(segmentName);
	}

	private CommandAction<?, ?> createCommandAction(Object target, Method method) {
		method.setAccessible(true);

		if (method.getParameterCount() == 0) {
			return Void.TYPE.equals(method.getReturnType()) ? CommandFactory.createRunnableAction(target, method)
					: CommandFactory.createSupplierAction(target, method);
		} else {
			return Void.TYPE.equals(method.getReturnType()) ? CommandFactory.createConsumerAction(target, method)
					: CommandFactory.createFunctionAction(target, method);
		}
	}

	private void registerCommand(CommandName commandName, CommandAction<?, ?> command, Set<SegmentName> permissions) {
		commands.put(commandName, command);
		commandPermissions.put(commandName, permissions);
	}

	private Set<SegmentName> getPermissions(SegmentName[] permissions) {
		return Optional.ofNullable(permissions).map(Arrays::asList)
										 .filter(list -> !list.isEmpty())
										 .map(EnumSet::copyOf)
										 .orElse(EnumSet.noneOf(SegmentName.class));
	}

}
