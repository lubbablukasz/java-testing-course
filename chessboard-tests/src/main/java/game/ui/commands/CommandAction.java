package game.ui.commands;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface CommandAction<T, R> {
	
    R execute(T input);

    static CommandAction<Void, Void> fromRunnable(Runnable runnable) {
        return input -> {
            runnable.run();
            return null;
        };
    }

    static <R> CommandAction<Void, R> fromSupplier(Supplier<R> supplier) {
        return input -> supplier.get();
    }

    static <T> CommandAction<T, Void> fromConsumer(Consumer<T> consumer) {
        return input -> {
            consumer.accept(input);
            return null;
        };
    }

    static <T, R> CommandAction<T, R> fromFunction(Function<T, R> function) {
        return function::apply;
    }
}
