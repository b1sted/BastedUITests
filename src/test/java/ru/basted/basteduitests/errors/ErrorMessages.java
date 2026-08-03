package ru.basted.basteduitests.errors;

public final class ErrorMessages {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final String MESSAGE_TEMPLATE = "[%s]%n%s";
    private static final String UNKNOWN_CONTEXT = "UnknownClass -> UnknownMethod";

    private ErrorMessages() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    public static String buildErrorMessage(String context, String reason) {
        return MESSAGE_TEMPLATE.formatted(context, reason);
    }

    public static String buildErrorMessage(String reason) {
        StackTraceElement caller = STACK_WALKER.walk(stream -> stream
                        .dropWhile(frame -> frame.getDeclaringClass() == ErrorMessages.class)
                        .findFirst())
                .map(StackWalker.StackFrame::toStackTraceElement)
                .orElse(null);

        String context = (caller == null)
                ? UNKNOWN_CONTEXT
                : "%s -> %s".formatted(caller.getClassName(), caller.getMethodName());

        return MESSAGE_TEMPLATE.formatted(context, reason);
    }

    public static String buildErrorMessage(String reason, String... contextSegments) {
        String context = (contextSegments == null || contextSegments.length == 0)
                ? UNKNOWN_CONTEXT
                : String.join(" -> ", contextSegments);

        return MESSAGE_TEMPLATE.formatted(context, reason);
    }
}
