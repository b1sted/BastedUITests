package ru.basted.basteduitests.config.converters;

import java.lang.reflect.Method;
import java.time.Duration;

import org.aeonbits.owner.Converter;

/**
 * Преобразует строковое значение секунд (без суффикса единицы измерения)
 * в {@link Duration}.
 * <p>
 * В отличие от стандартного DSL-парсера длительностей Owner, число без
 * суффикса здесь трактуется как секунды, а не как миллисекунды.
 */
public class SecondsDurationConverter implements Converter<Duration> {
    /**
     * @throws IllegalArgumentException если {@code input} пуст, null
     *                                  или не является целым числом
     */
    @Override
    public Duration convert(Method method, String input) {
        ConverterUtils.requireNonBlank(method, input);

        long seconds;
        try {
            seconds = Long.parseLong(input);
        } catch (NumberFormatException ex) {
            throw ConverterUtils.invalidValue(method, input, "число секунд, большее нуля", ex);
        }

        if (seconds <= 0) {
            throw ConverterUtils.invalidValue(method, input, "число секунд, большее нуля", null);
        }

        return Duration.ofSeconds(seconds);
    }
}
