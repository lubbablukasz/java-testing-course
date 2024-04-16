package game.ui.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import game.ui.segment.general.SegmentName;

/**
 * Annotation used to mark methods designated to be ran by ui segments
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

	CommandName name();

	SegmentName[] permissions() default {};
}
