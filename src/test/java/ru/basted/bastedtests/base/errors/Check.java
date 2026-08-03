package ru.basted.bastedtests.base.errors;

/**
 * Контракт для видов проверок, используемых с {@link ru.basted.bastedtests.base.errors.PageAssertions PageAssertions}.
 * <p>
 * Реализации (например, {@code enum}-константы) должны обеспечивать согласованный
 * формат сообщений об ошибках, пригодный для передачи в
 * {@link ru.basted.bastedtests.errors.ErrorMessages#buildErrorMessage(String, String)}.
 */
public interface Check {
    /**
     * Формирует контекст проверки для сообщения об ошибке (например, раздел сайта,
     * конкретный элемент или проверяемый параметр — в зависимости от вида проверки).
     * Не должен содержать завершающий перенос строки — он добавляется на уровне
     * {@link ru.basted.bastedtests.errors.ErrorMessages}.
     *
     * @param pageState состояние, для которого выполняется проверка
     * @return строка контекста в формате, специфичном для конкретной реализации {@link Check}
     */
    String context(PageState pageState);

    /**
     * Формирует текст ошибки о несовпадении значений.
     *
     * @param expected ожидаемое значение
     * @param actual   фактическое значение
     * @return Текст ошибки, например {@code "Ожидался URL '...', но получен '...'"}
     */
    String failMessage(Object expected, Object actual);
}