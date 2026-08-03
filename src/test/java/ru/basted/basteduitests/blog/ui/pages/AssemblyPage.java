package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.BasePage;
import ru.basted.basteduitests.base.capabilities.HasArticles;
import ru.basted.basteduitests.base.capabilities.HasBackButton;

@PageInfo(siteEntity = "Блог", name = "Изучаем Assembly NASM")
@PagePath("/assembly")
public final class AssemblyPage extends BasePage<AssemblyPage> implements HasArticles, HasBackButton {
    private final By expandButtonLocator = By.cssSelector("button[class*='assembly']");

    public AssemblyPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return expandButtonLocator;
    }
}
