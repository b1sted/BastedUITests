package ru.basted.bastedtests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.bastedtests.annotations.PageInfo;
import ru.basted.bastedtests.annotations.PagePath;
import ru.basted.bastedtests.base.BasePage;
import ru.basted.bastedtests.base.capabilities.HasArticles;
import ru.basted.bastedtests.base.capabilities.HasBackButton;

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
