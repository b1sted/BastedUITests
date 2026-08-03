package ru.basted.bastedtests.base.capabilities;

import org.openqa.selenium.By;

import ru.basted.bastedtests.base.BasePage;

public interface HasArticles {
    By ARTICLE_CARD_LOCATOR = By.cssSelector("article.card a");

    default By getArticleCardLocator() {
        return ARTICLE_CARD_LOCATOR;
    }

    default <T extends BasePage<T>> T openArticle(Class<T> targetPageClass) {
        BasePage<?> self = (BasePage<?>) this;
        self.click(getArticleCardLocator());
        return self.as(targetPageClass);
    }
}
