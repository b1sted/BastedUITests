package ru.basted.basteduitests.base.capabilities;

import java.util.function.Function;

import ru.basted.basteduitests.base.AbstractPageCore;
import ru.basted.basteduitests.base.errors.PageState;
import ru.basted.basteduitests.errors.ErrorMessages;

public final class PageCapabilities {
    private PageCapabilities() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    public static <I, R> R requireSupports(
            Object page,
            Class<I> capability,
            Function<I, R> action
    ) {
        if (!capability.isInstance(page)) {
            AbstractPageCore<?> pageObject = (AbstractPageCore<?>) page;
            PageState state = pageObject.state();

            throw new UnsupportedOperationException(
                    ErrorMessages.buildErrorMessage("Класс '%s' не имплементирует интерфейс '%s'"
                                    .formatted(capability.getSimpleName(), page.getClass().getSimpleName()),
                            state.siteEntity(), state.pageTitle(), "Архитектура фреймворка")
            );
        }

        return action.apply(capability.cast(page));
    }
}
