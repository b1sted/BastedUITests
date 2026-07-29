package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.BasePage;

@PageInfo(siteEntity = "Блог", title = "Изучаем Assembly NASM")
@PagePath("/assembly")
public final class AssemblyPage extends BasePage<AssemblyPage> {
    private final By expandButtonLocator = By.cssSelector("button[class*='assembly']");

    public AssemblyPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return expandButtonLocator;
    }
}
