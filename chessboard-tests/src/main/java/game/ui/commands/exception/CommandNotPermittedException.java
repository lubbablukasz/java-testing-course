package game.ui.commands.exception;

import game.ui.commands.CommandName;
import game.ui.segment.general.SegmentName;

public class CommandNotPermittedException extends RuntimeException {

	private static final String EXCEPTION_MSG = "Command not avaliable for requesting segment ";

	public CommandNotPermittedException(CommandName commandName, SegmentName segmentName) {
		super(EXCEPTION_MSG + segmentName);
	}
}
