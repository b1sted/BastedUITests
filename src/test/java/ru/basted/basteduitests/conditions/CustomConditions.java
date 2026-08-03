package ru.basted.basteduitests.conditions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

@SuppressWarnings("NullAway")
public final class CustomConditions {
    private CustomConditions() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    /**
     * Ожидание для проверки того, что текущий URL страницы изменился и больше не равен указанному.
     *
     * @param invalidUrl исходный URL, с которого должен произойти переход (старый URL)
     * @return Новый текущий URL страницы, как только он перестанет совпадать с {@code invalidUrl};
     *         иначе {@code null}, если URL еще не изменился
     */
    public static ExpectedCondition<String> urlToChangeFrom(String invalidUrl) {
        return new ExpectedCondition<>() {
            @Override
            public String apply(WebDriver webDriver) {
                String currentUrl = webDriver.getCurrentUrl();
                return invalidUrl.equals(currentUrl) ? null : currentUrl;
            }

            @Override
            public String toString() {
                return "url to change from %s".formatted(invalidUrl);
            }
        };
    }
}
