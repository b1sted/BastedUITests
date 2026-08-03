package ru.basted.bastedtests.base.capabilities;

import org.openqa.selenium.By;

import ru.basted.bastedtests.base.BasePage;

public interface HasBackButton {
    By BACK_BUTTON_LOCATOR = By.cssSelector("a[class*='back']");

    default By getBackButtonLocator() {
        return BACK_BUTTON_LOCATOR;
    }

    default <T extends BasePage<T>> T clickBackButton(Class<T> targetPageClass) {
        BasePage<?> self = (BasePage<?>) this;
        self.click(getBackButtonLocator());
        return self.as(targetPageClass);
    }
}