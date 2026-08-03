package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.BasePage;
import ru.basted.basteduitests.base.capabilities.HasArticles;
import ru.basted.basteduitests.base.capabilities.HasBackButton;

@PageInfo(siteEntity = "Блог", name = "Блог")
@PagePath("/blog")
public final class BlogPage extends BasePage<BlogPage> implements HasArticles, HasBackButton {
    private final By sectionTitleLocator = By.cssSelector("h2[class*='section']");

    public BlogPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return sectionTitleLocator;
    }
}
