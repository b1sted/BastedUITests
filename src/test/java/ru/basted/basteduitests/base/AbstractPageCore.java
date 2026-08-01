package ru.basted.basteduitests.base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.basted.basteduitests.annotations.AnnotationUtils;
import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.errors.PageState;
import ru.basted.basteduitests.config.Configs;
import ru.basted.basteduitests.errors.ErrorMessages;

/**
 * Базовый класс для всех Page Object'ов: страниц тестируемого сайта
 * и внешних страниц.
 * <p>
 * Инкапсулирует {@link WebDriver}/{@link WebDriverWait} и предоставляет
 * общие обёртки над Selenium (навигация, клики, ожидания, вкладки, URL).
 * <p>
 * Self-referencing generic {@code T} нужен для fluent-цепочек
 * ({@link #open(Object...)}, {@link #as(Class)} возвращают конкретный тип страницы).
 */
public abstract class AbstractPageCore<T extends AbstractPageCore<T>> {
    protected final WebDriver webDriver;
    protected final WebDriverWait webDriverWait;

    protected AbstractPageCore(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.webDriverWait = new WebDriverWait(webDriver, Configs.project().timeout());
    }

    /**
     * Открывает страницу в браузере по URL, построенному из аннотации {@link PagePath}.
     *
     * @param pathParams значения для плейсхолдеров {@code %s} в шаблоне {@link PagePath},
     *                    если путь страницы параметризован (например, {@code "/blog/%s"})
     */
    @SuppressWarnings("unchecked")
    public T open(Object... pathParams) {
        webDriver.get(resolveUrl(pathParams));
        return (T) this;
    }

    /**
     * Создаёт объект целевой страницы через reflection.
     *
     * @param pageClass класс страницы; обязан иметь публичный конструктор,
     *                   принимающий единственный параметр {@link WebDriver}
     * @throws RuntimeException если у {@code pageClass} нет такого конструктора
     *                           или его вызов завершился ошибкой
     */
    protected <P extends AbstractPageCore<P>> P as(Class<P> pageClass) {
        try {
            return pageClass.getDeclaredConstructor(WebDriver.class).newInstance(webDriver);
        } catch (Exception ex) {
            throw new RuntimeException(
                    ErrorMessages.buildErrorMessage("Ошибка инициализации страницы: " + pageClass.getSimpleName()), ex
            );
        }
    }

    /**
     * Переходит на предыдущую страницу в истории браузера через
     * {@link org.openqa.selenium.WebDriver.Navigation#back()} и трактует результат
     * как страницу типа {@code pageClass}.
     * <p>
     * Не проверяет, что после перехода вы действительно оказались на этой странице —
     * ответственность за корректность типа лежит на вызывающем коде. Для проверки
     * фактической загрузки страницы используйте {@link BasePage#isPageLoaded()} после вызова.
     *
     * @param pageClass ожидаемый класс страницы после перехода назад
     */
    public <P extends AbstractPageCore<P>> P navigateBack(Class<P> pageClass) {
        webDriver.navigate().back();
        return this.as(pageClass);
    }

    /**
     * Возвращает текущий URL страницы согласно браузеру.
     *
     * @throws IllegalStateException если Selenium WebDriver вернул {@code null}
     *                                вместо текущего URL страницы
     */
    public String getCurrentUrl() {
        String url = webDriver.getCurrentUrl();
        if (url == null) {
            throw new IllegalStateException(
                    ErrorMessages.buildErrorMessage("Selenium WebDriver вернул null вместо текущего URL страницы")
            );
        }

        return url;
    }

    /**
     * Возвращает URL, на который должна была перейти страница — строится
     * так же, как в {@link #open(Object...)}, но без реальной навигации.
     * Используется для сравнения с фактическим URL браузера в проверках.
     *
     * @param pathParams значения для плейсхолдеров {@code %s} в шаблоне {@link PagePath}
     */
    public String getExpectedUrl(Object... pathParams) {
        return resolveUrl(pathParams);
    }

    protected String getPageTitle() {
        PageInfo annotation = AnnotationUtils.getAnnotation(this.getClass(), PageInfo.class);
        return (annotation != null) ? annotation.title() : "Неизвестная страница";
    }

    public PageState state() {
        PageInfo annotation = AnnotationUtils.getAnnotation(this.getClass(), PageInfo.class);
        if (annotation == null) {
            return new PageState("Неизвестная сущность", "Неизвестная страница");
        }

        return new PageState(annotation.siteEntity(), annotation.title());
    }

    public String switchToNewTab() {
        Object[] windowHandles = webDriver.getWindowHandles().toArray();

        String newTab = (String) windowHandles[windowHandles.length - 1];
        String previousTab = (String) windowHandles[windowHandles.length - 2];

        switchToTab(newTab);

        return previousTab;
    }

    public void switchToTab(String windowHandle) {
        webDriver.switchTo().window(windowHandle);
    }

    public void closeCurrentTab() {
        webDriver.close();
    }

    protected void click(By locator) {
        WebElement element = waitForClickable(locator);
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

    protected WebElement waitForClickable(By locator) {
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean isElementVisible(By locator) {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    /**
     * Строит полный URL страницы: базовый URL из конфигурации + путь из {@link PagePath},
     * подставляя {@code pathParams} в плейсхолдеры {@code %s} шаблона пути.
     * <p>
     * Гарантирует наличие завершающего слэша в результирующем URL, так как сайт
     * всегда отдаёт редирект на версию с {@code /} в конце.
     *
     * @throws IllegalStateException если у класса страницы отсутствует аннотация {@link PagePath}
     */
    private String resolveUrl(Object... pathParams) {
        PagePath pageUrl = AnnotationUtils.getAnnotation(this.getClass(), PagePath.class);
        if (pageUrl == null) {
            throw new IllegalStateException(ErrorMessages.buildErrorMessage("У класса нет аннотации @PagePath!"));
        }

        String pathTemplate = pageUrl.value();
        String url = Configs.project().baseUrl() + String.format(pathTemplate, pathParams);

        boolean hasTrailingSlash = url.charAt(url.length() - 1) == '/';
        return hasTrailingSlash ? url : url + '/';
    }
}
