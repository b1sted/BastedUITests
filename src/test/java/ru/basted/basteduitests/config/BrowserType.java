package ru.basted.basteduitests.config;

/**
 * Доступные движки браузеров для проведения автоматизированных тестов.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    /**
     * Преобразует строку в {@link BrowserType}, игнорируя регистр символов
     * (в отличие от стандартного {@link Enum#valueOf}).
     *
     * @throws IllegalArgumentException если {@code input} null или
     *                                  не соответствует ни одному из значений enum
     */
    public static BrowserType valueOfIgnoreCase(String input) {
        String normalizedValue = input.trim().toUpperCase();
        return BrowserType.valueOf(normalizedValue);
    }
}
