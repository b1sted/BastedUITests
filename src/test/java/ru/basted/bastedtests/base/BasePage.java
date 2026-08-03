package ru.basted.bastedtests.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.bastedtests.config.Configs;
import ru.basted.bastedtests.errors.ErrorMessages;

/**
 * Базовый класс для страниц тестируемого сайта.
 * <p>
 * Добавляет к {@link AbstractPageCore} контракт "уникального элемента" —
 * каждый наследник должен указать локатор, по которому определяется,
 * что страница загрузилась ({@link #isPageLoaded()}).
 * <p>
 * Внешние страницы (сторонние сервисы) наследуются от {@link AbstractPageCore}
 * напрямую, минуя этот класс.
 */
public abstract class BasePage<T extends BasePage<T>> extends AbstractPageCore<T> {
    protected BasePage(WebDriver webDriver) {
        super(webDriver);
    }

    /**
     * Возвращает локатор элемента, уникального для данной страницы.
     * <p>
     * По этому локатору {@link #isPageLoaded()} определяет, что страница
     * действительно загрузилась — элемент должен присутствовать только
     * на этой конкретной странице и появляться после полной загрузки.
     */
    protected abstract By getUniqueElement();

    /**
     * Дожидается видимости уникального элемента страницы ({@link #getUniqueElement()})
     * в течение тайм-аута из конфигурации.
     *
     * @throws RuntimeException если элемент не стал видимым за отведённое время
     */
    @SuppressWarnings("unchecked")
    public T isPageLoaded() {
        By uniqueElementLocator = getUniqueElement();

        if (!isElementVisible(uniqueElementLocator)) {
            throw new RuntimeException(
                    ErrorMessages.buildErrorMessage("За %d секунд страница '%s' (URL: %s) не загрузилась"
                            .formatted(Configs.project().timeout().toSeconds(), getPageName(), getCurrentUrl()))
            );
        }

        return (T) this;
    }
}