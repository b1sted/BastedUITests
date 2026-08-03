package ru.basted.bastedtests.config.converters;

import java.lang.reflect.Method;

import org.jspecify.annotations.Nullable;

/**
 * Вспомогательные методы валидации для реализаций {@link org.aeonbits.owner.Converter}.
 */
final class ConverterUtils {
    private ConverterUtils() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    /**
     * Проверяет, что входное значение не null и не пусто.
     *
     * @throws IllegalArgumentException если {@code input} null или состоит только из пробелов
     */
    static void requireNonBlank(Method method, @Nullable String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(
                    "Ошибка конфигурации: значение для метода %s.%s не может быть пустым (null)!"
                            .formatted(method.getDeclaringClass().getSimpleName(), method.getName())
            );
        }
    }

    /**
     * Формирует исключение о недопустимом значении конфигурации.
     *
     * @param method      метод конфигурационного интерфейса ({@link org.aeonbits.owner.Config}),
     *                     для которого не удалось получить корректное значение;
     *                     используется для указания класса и имени метода в сообщении
     * @param input        исходное строковое значение из конфигурации, которое не прошло проверку
     * @param requirement  описание требования к значению в свободной форме
     *                      (например, {@code "число секунд, большее нуля"} или
     *                      {@code "одно из значений: [CHROME, FIREFOX, EDGE]"}),
     *                      подставляется в текст сообщения
     * @param cause         исходная причина ошибки (например, {@link NumberFormatException}
     *                      при нечисловом входе), либо {@code null}, если входное значение
     *                      синтаксически корректно, но не удовлетворяет {@code requirement}
     * @return Сформированное исключение с описательным сообщением, готовое к выбросу
     */
    static IllegalArgumentException invalidValue(
            Method method,
            String input,
            String requirement,
            @Nullable Throwable cause
    ) {
        String message = "В %s.%s ожидалось %s, но пришло: '%s'"
                .formatted(method.getDeclaringClass().getSimpleName(), method.getName(), requirement, input);
        return cause != null ? new IllegalArgumentException(message, cause) : new IllegalArgumentException(message);
    }
}
