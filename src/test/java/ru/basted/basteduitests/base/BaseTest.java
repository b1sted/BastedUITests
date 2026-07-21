package ru.basted.basteduitests.base;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.jspecify.annotations.Nullable;

import ru.basted.basteduitests.config.BrowserType;
import ru.basted.basteduitests.config.Configs;

/**
 * Базовый класс для всех тест-кейсов в проекте.
 * <p>
 * Отвечает за конфигурацию окружения, а также автоматическое создание
 * и закрытие сессий WebDriver (браузера) до и после каждого теста.
 */
public abstract class BaseTest {
    @Nullable
    protected WebDriver webDriver;

    @BeforeEach
    public void setup() {
        BrowserType browser = Configs.project().browser();
        boolean isHeadless = Configs.project().isHeadless();

        switch (browser) {
            case CHROME -> webDriver = new ChromeDriver(withChromiumArgs(new ChromeOptions(), isHeadless));
            case EDGE -> webDriver = new EdgeDriver(withChromiumArgs(new EdgeOptions(), isHeadless));
            case FIREFOX -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("-private");

                if (isHeadless) {
                    options.addArguments("-headless");
                    options.addPreference("browser.window.width", 1920);
                    options.addPreference("browser.window.height", 1080);
                }

                webDriver = new FirefoxDriver(options);
            }
            default -> throw invalidBrowserArgument(browser, Arrays.toString(BrowserType.values()));
        }

        if (!isHeadless) {
            webDriver.manage().window().maximize();
        }
    }

    @AfterEach
    public void teardown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    private static <T extends ChromiumOptions<T>> T withChromiumArgs(T options, boolean isHeadless) {
        options.addArguments("--incognito");
        if (isHeadless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static IllegalArgumentException invalidBrowserArgument(BrowserType input, String requirements) {
        return new IllegalArgumentException(
                "Неверно указан тип браузера в конфигурации. Передано: '%s'. Допустимые значения: %s"
                        .formatted(input, requirements)
        );
    }
}
