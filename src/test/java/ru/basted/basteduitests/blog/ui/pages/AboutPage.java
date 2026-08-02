package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.annotations.PagePath;
import ru.basted.basteduitests.base.BasePage;
import ru.basted.basteduitests.base.capabilities.HasBackButton;

@PageInfo(siteEntity = "Блог", name = "Обо мне")
@PagePath("/about")
public final class AboutPage extends BasePage<AboutPage> implements HasBackButton {
    private final By academicToggleLabelLocator = By.cssSelector("label[class*='academic']");

    public AboutPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected By getUniqueElement() {
        return academicToggleLabelLocator;
    }
}
