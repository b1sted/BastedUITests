package ru.basted.bastedtests.config;

import java.time.Duration;

import org.aeonbits.owner.Config;

import ru.basted.bastedtests.config.converters.BrowserTypeConverter;
import ru.basted.bastedtests.config.converters.SecondsDurationConverter;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {
    @Key("base.url")
    @DefaultValue("https://basted.ru")
    String baseUrl();

    /**
     * Таймаут явных ожиданий (Explicit Wait) для {@link org.openqa.selenium.support.ui.WebDriverWait}.
     * <p>
     * Значение указывается в секундах без суффикса единицы измерения
     * (например, {@code 10}) благодаря {@link SecondsDurationConverter} —
     * стандартный DSL-парсер Owner трактовал бы такое значение как миллисекунды.
     */
    @Key("timeout.seconds")
    @DefaultValue("10")
    @ConverterClass(SecondsDurationConverter.class)
    Duration timeout();

    /**
     * Тип браузера, используемый для UI-тестов.
     * <p>
     * Значение регистронезависимо (например, {@code chrome} и {@code CHROME}
     * равнозначны) благодаря {@link BrowserTypeConverter}.
     */
    @Key("browser")
    @DefaultValue("chrome")
    @ConverterClass(BrowserTypeConverter.class)
    BrowserType browser();

    @Key("headless")
    @DefaultValue("true")
    boolean isHeadless();
}
