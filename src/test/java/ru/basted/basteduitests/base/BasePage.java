package ru.basted.basteduitests.base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.basted.basteduitests.config.Configs;

/**
 * Базовый класс для всех страниц (Page Objects) в проекте.
 * <p>
 * Предоставляет общие методы для взаимодействия с элементами страницы
 * с автоматическим применением явных ожиданий (Explicit Waits).
 */
public abstract class BasePage {
    protected final WebDriver webDriver;
    protected final WebDriverWait webDriverWait;

    public BasePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.webDriverWait = new WebDriverWait(webDriver, Configs.project().timeout());
    }

    protected void open(String relativeUrl) {
        webDriver.get(Configs.project().baseUrl() + relativeUrl);
    }

    protected void click(By locator) {
        WebElement element = webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
        click(element);
    }

    /**
     * Кликает по элементу, повторяя попытку через {@link Actions#moveToElement},
     * если стандартный клик перехвачен другим элементом
     * ({@link ElementClickInterceptedException}).
     */
    protected void click(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException ex) {
            new Actions(webDriver)
                    .moveToElement(element)
                    .click()
                    .perform();
        }
    }

    protected WebElement find(By locator) {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
