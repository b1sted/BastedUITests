package ru.basted.basteduitests.config.converters;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aeonbits.owner.Converter;

import ru.basted.basteduitests.config.BrowserType;

/**
 * Преобразует строковое значение браузера из {@code config.properties}
 * в {@link BrowserType}, игнорируя регистр символов.
 */
public class BrowserTypeConverter implements Converter<BrowserType> {
    /**
     * @throws IllegalArgumentException если {@code input} null или
     *                                  не соответствует ни одному из {@link BrowserType}
     */
    @Override
    public BrowserType convert(Method method, String input) {
        ConverterUtils.requireNonBlank(method, input);

        try {
            return BrowserType.valueOfIgnoreCase(input);
        } catch (IllegalArgumentException ex) {
            throw ConverterUtils.invalidValue(
                    method, input, "одно из значений: %s".formatted(Arrays.toString(BrowserType.values())), ex
            );
        }
    }
}
