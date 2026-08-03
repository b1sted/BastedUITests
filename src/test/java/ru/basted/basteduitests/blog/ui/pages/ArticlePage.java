package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.base.BasePage;

@PageInfo(siteEntity = "Блог", name = "Статья")
public final class ArticlePage extends BasePage<ArticlePage> {
    private final By articleMetaDivisionLocator = By.cssSelector("div[class='article-meta']");
    private final By articleTitleLocator = By.cssSelector("h1[class*='title']");

    public ArticlePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return articleMetaDivisionLocator;
    }

    public String getArticleTitle() {
        return this.getText(articleTitleLocator);
    }
}
