package game.ui.commands;

import java.lang.reflect.Method;

public class CommandFactory {

	private CommandFactory() {
	}
	
	public static CommandAction<Void, Void> createRunnableAction(Object target, Method method) {
		return CommandAction.fromRunnable(() -> {
			try {
                method.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException("Error registering runnable command", e);
            }
		});
	}
	
	public static CommandAction<Void, ?> createSupplierAction(Object target, Method method) {
		return CommandAction.fromSupplier(() -> {
			try {
				return method.invoke(target);
			} catch (Exception e) {
				throw new RuntimeException("Error registering supplier command", e);
			} 
		});
	}
	
	public static CommandAction<?, Void> createConsumerAction(Object target, Method method) {
		return CommandAction.fromConsumer(param -> {
			try {
                method.invoke(target, param);
            } catch (Exception e) {
                throw new RuntimeException("Error registering consumer command", e);
            }
		});
	}
	
	public static CommandAction<?, ?> createFunctionAction(Object target, Method method) {
		return CommandAction.fromFunction(param -> {
			try {
				return method.invoke(target, param);
			} catch (Exception e) {
				throw new RuntimeException("Error registering function command", e);
			} 
		});
	}
}
