package ru.basted.basteduitests.blog.ui;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.basted.basteduitests.base.AbstractPageCore;
import ru.basted.basteduitests.base.BasePage;
import ru.basted.basteduitests.base.BaseTest;
import ru.basted.basteduitests.base.capabilities.HasArticles;
import ru.basted.basteduitests.base.capabilities.HasBackButton;
import ru.basted.basteduitests.base.capabilities.PageCapabilities;
import ru.basted.basteduitests.base.errors.PageAssertions;
import ru.basted.basteduitests.base.errors.PageCheck;
import ru.basted.basteduitests.base.errors.PageState;
import ru.basted.basteduitests.blog.ui.pages.AboutPage;
import ru.basted.basteduitests.blog.ui.pages.ArticlePage;
import ru.basted.basteduitests.blog.ui.pages.AssemblyPage;
import ru.basted.basteduitests.blog.ui.pages.BlogPage;
import ru.basted.basteduitests.blog.ui.pages.ExternalPage;
import ru.basted.basteduitests.blog.ui.pages.MainPage;
import ru.basted.basteduitests.errors.ErrorMessages;

@DisplayName("Главная страница")
public final class MainPageTest extends BaseTest {
    @Test
    @DisplayName("BLOG-T2: Навигация по разделам с главной страницы")
    public void shouldNavigateToSectionsFromMainPage() {
        final Map<String, Class<? extends AbstractPageCore<?>>> sections = Map.of(
                "Блог", BlogPage.class,
                "Ассемблер", AssemblyPage.class,
                "Обо мне", AboutPage.class
        );

        SoftAssertions softly = new SoftAssertions();

        MainPage mainPage = new MainPage(webDriver)
                .open()
                .isPageLoaded();

        for (Map.Entry<String, Class<? extends AbstractPageCore<?>>> entry : sections.entrySet()) {
            String ariaLabel = entry.getKey();
            Class<? extends AbstractPageCore<?>> expectedPageClass = entry.getValue();

            @SuppressWarnings({"unchecked", "rawtypes"})
            AbstractPageCore<?> genericPage = mainPage.clickNavigationMenuLink(ariaLabel, (Class) expectedPageClass);

            String expectedUrl = genericPage.getExpectedUrl();
            String currentUrl = genericPage.getCurrentUrl();

            PageAssertions.softAssertPageEquals(softly, genericPage.state(), PageCheck.URL, expectedUrl, currentUrl);

            if (genericPage instanceof HasBackButton backButtonPage) {
                mainPage = backButtonPage.clickBackButton(MainPage.class);
            } else {
                PageState state = genericPage.state();
                throw new UnsupportedOperationException(
                        ErrorMessages.buildErrorMessage("Класс '%s' не имплементирует интерфейс '%s' (кнопка 'Назад' недоступна)"
                                        .formatted(genericPage.getClass().getSimpleName(), HasBackButton.class.getSimpleName()),
                                state.siteEntity(), state.pageTitle(), "Архитектура фреймворка")
                );
            }

            mainPage.isPageLoaded();
        }

        softly.assertAll();
    }

    @Test
    @DisplayName("BLOG-T3: Внешние ссылки главного меню")
    public void shouldRedirectToExternalWebsitesWhenUserClicksLink() {
        final Map<String, String> redirectButtons = Map.of(
                "Проекты", "github.com",
                "Конспекты", "basted.ru",
                "Поддержать", "pay.cloudtips.ru"
        );

        SoftAssertions softly = new SoftAssertions();

        MainPage mainPage = new MainPage(webDriver)
                .open()
                .isPageLoaded();

        for (Map.Entry<String, String> redirectButton : redirectButtons.entrySet()) {
            String ariaLabel = redirectButton.getKey();
            String domainToBeContain = redirectButton.getValue();

            ExternalPage externalPage = mainPage.clickNavigationMenuLink(ariaLabel, ExternalPage.class);
            String previousTab = externalPage.switchToNewTab();

            String currentUrl = externalPage.getCurrentUrl();
            PageAssertions.softAssertPageContains(softly, externalPage.state(), PageCheck.URL, domainToBeContain, currentUrl);

            externalPage.closeCurrentTab();
            externalPage.switchToTab(previousTab);
        }
    }

    @Test
    @DisplayName("BLOG-T4: Открытие контентных статей")
    public void shouldOpenArticleWhenUserClicksOnCard() {
        final List<Class<? extends AbstractPageCore<?>>> pagesWithArticles = List.of(AssemblyPage.class, BlogPage.class);

        SoftAssertions softly = new SoftAssertions();

        for (Class<? extends AbstractPageCore<?>> pageClass : pagesWithArticles) {
            @SuppressWarnings({"unchecked", "rawtypes"})
            BasePage<?> pageWithArticles = ((BasePage<?>) AbstractPageCore.instantiate((Class) pageClass, webDriver))
                    .open()
                    .isPageLoaded();

            ArticlePage articlePage = PageCapabilities.requireSupports(pageWithArticles, HasArticles.class,
                    page -> page.openArticle(ArticlePage.class)).isPageLoaded();
            PageState articleState = articlePage.state();

            String expectedUrl = Pattern.quote(pageWithArticles.getExpectedUrl()) + "[a-zA-Z0-9-]+/?";
            String currentUrl = articlePage.getCurrentUrl();
            PageAssertions.softAssertPageMatches(softly, articleState, PageCheck.URL, expectedUrl, currentUrl);

            String articleTitle = articlePage.getArticleTitle();
            String pageTitle = formatPageTitleToComparison(articlePage.getPageTitle());
            PageAssertions.softAssertPageMatches(softly, articleState, PageCheck.TITLE, articleTitle, pageTitle);
        }

        softly.assertAll();
    }

    private String formatPageTitleToComparison(String pageTitle) {
        int endIndex = pageTitle.indexOf('|');
        return pageTitle.substring(0, endIndex - 1);
    }
}
