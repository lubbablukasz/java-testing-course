package game.ui.commands;

import game.ui.segment.general.UiSegment;

public interface CommandExecutor {

	<R> R executeCommand(CommandName commandName, UiSegment requestingSegment);
	
	<T, R> R executeCommand(CommandName commandName, UiSegment requestingSegment, T input);
}
