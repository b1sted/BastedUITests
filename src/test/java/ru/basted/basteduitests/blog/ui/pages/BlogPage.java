package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.BasePage;

@PageInfo(siteEntity = "Блог", title = "Блог")
@PagePath("/blog")
public final class BlogPage extends BasePage<BlogPage> {
    private final By sectionTitleLocator = By.cssSelector("h2[class*='section']");

    public BlogPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return sectionTitleLocator;
    }
}
