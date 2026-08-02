package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.AbstractPageCore;
import ru.basted.basteduitests.base.BasePage;
import ru.basted.basteduitests.base.capabilities.HasBackButton;

@PageInfo(siteEntity = "Блог", name = "Главная страница")
@PagePath("/")
public final class MainPage extends BasePage<MainPage> implements HasBackButton {
    private final String navigationMenuLinkTemplate = "div .nav-menu a[aria-label*='%s']";

    public MainPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return By.cssSelector(navigationMenuLinkTemplate.formatted("Поддержать"));
    }

    /**
     * Кликает по конкретной ссылке из навигационного меню по её атрибуту aria-label
     * и динамически инициализирует класс ожидаемой страницы.
     *
     * @param ariaLabel значение атрибута aria-label нужной ссылки
     * @param expectedPageClass класс страницы, на которую должен произойти переход
     * @return Объект целевой страницы
     */
    public <P extends AbstractPageCore<P>> P clickNavigationMenuLink(
            String ariaLabel,
            Class<P> expectedPageClass
    ) {
        By navigationMenuLinkLocator = By.cssSelector(navigationMenuLinkTemplate.formatted(ariaLabel));
        click(navigationMenuLinkLocator);
        return this.as(expectedPageClass);
    }
}
