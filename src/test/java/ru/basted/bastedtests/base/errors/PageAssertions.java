package ru.basted.bastedtests.base.errors;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.SoftAssertions;

import ru.basted.bastedtests.errors.ErrorMessages;

import static org.assertj.core.api.Assertions.assertThat;

public final class PageAssertions {
    private PageAssertions() {
        throw new UnsupportedOperationException("Запрещено создание экземпляра утилитарного класса");
    }

    /**
     * Проверяет равенство {@code actual} и {@code expected}, немедленно бросая
     * {@link AssertionError}, если значения не совпадают.
     * <p>
     * Сообщение об ошибке формируется из {@link Check#context(PageState)}
     * (контекст: раздел сайта, страница, проверяемый параметр) и
     * {@link Check#failMessage(Object, Object)} (что ожидалось и что получено).
     *
     * @param pageState состояние страницы (раздел сайта, заголовок), используется для контекста ошибки
     * @param check     тип проверки, определяющий формат контекста и текста ошибки
     * @param expected  ожидаемое значение
     * @param actual    фактическое значение
     * @return Fluent-assert AssertJ для дальнейшей цепочки вызовов
     */
    public static AbstractStringAssert<?> assertPageEquals(
            PageState pageState,
            Check check,
            Object expected,
            Object actual
    ) {
        return assertThat(actual.toString())
                .withFailMessage(buildFailMessage(pageState, check, expected, actual))
                .isEqualTo(expected.toString());
    }

    /**
     * Аналог {@link #assertPageEquals(PageState, Check, Object, Object)}, но не бросает
     * исключение немедленно — несовпадение накапливается в {@code softly} и проявится
     * только при вызове {@link SoftAssertions#assertAll()}.
     * <p>
     * Используется, когда нужно собрать результаты нескольких независимых проверок
     * за один прогон (например, в цикле по разделам сайта), а не прерывать выполнение
     * на первом же несовпадении.
     *
     * @param softly    накопитель проверок; {@link SoftAssertions#assertAll()} должен быть
     *                  вызван вызывающим кодом после всех проверок
     * @param pageState состояние страницы (раздел сайта, заголовок), используется для контекста ошибки
     * @param check     тип проверки, определяющий формат контекста и текста ошибки
     * @param expected  ожидаемое значение
     * @param actual    фактическое значение
     */
    public static void softAssertPageEquals(
            SoftAssertions softly,
            PageState pageState,
            Check check,
            Object expected,
            Object actual
    ) {
        softly.assertThat(actual.toString())
                .withFailMessage(buildFailMessage(pageState, check, expected, actual))
                .isEqualTo(expected.toString());
    }

    /**
     * Аналог {@link #softAssertPageEquals(SoftAssertions, PageState, Check, Object, Object)},
     * но проверяет не точное равенство, а вхождение {@code expected} в {@code actual}
     * (см. {@link AbstractStringAssert#contains(CharSequence...)}).
     * <p>
     * Несовпадение накапливается в {@code softly} и проявится только при вызове
     * {@link SoftAssertions#assertAll()}.
     *
     * @param softly    накопитель проверок; {@link SoftAssertions#assertAll()} должен быть
     *                  вызван вызывающим кодом после всех проверок
     * @param pageState состояние страницы (раздел сайта, заголовок), используется для контекста ошибки
     * @param check     тип проверки, определяющий формат контекста и текста ошибки
     * @param expected  ожидаемая подстрока
     * @param actual    фактическое значение, в котором ищется подстрока
     */
    public static void softAssertPageContains(
            SoftAssertions softly,
            PageState pageState,
            Check check,
            Object expected,
            Object actual
    ) {
        softly.assertThat(actual.toString())
                .withFailMessage(buildFailMessage(pageState, check, expected, actual))
                .contains(expected.toString());
    }

    /**
     * Аналог {@link #softAssertPageContains(SoftAssertions, PageState, Check, Object, Object)},
     * но проверяет соответствие {@code actual} регулярному выражению из {@code expected}
     * (см. {@link AbstractStringAssert#matches(CharSequence)}).
     * <p>
     * Несовпадение накапливается в {@code softly} и проявится только при вызове
     * {@link SoftAssertions#assertAll()}.
     * <p>
     * Используется, когда сравнение должно быть не точным (см. {@link #softAssertPageEquals})
     * и не по вхождению подстроки (см. {@link #softAssertPageContains}), а по шаблону —
     * например, для URL с динамическим slug статьи или для заголовка страницы
     * с переменным суффиксом.
     *
     * @param softly    накопитель проверок; {@link SoftAssertions#assertAll()} должен быть
     *                  вызван вызывающим кодом после всех проверок
     * @param pageState состояние страницы (раздел сайта, заголовок), используется для контекста ошибки
     * @param check     тип проверки, определяющий формат контекста и текста ошибки
     * @param expected  регулярное выражение, которому должно соответствовать значение
     * @param actual    фактическое значение, проверяемое на соответствие шаблону
     */
    public static void softAssertPageMatches(
            SoftAssertions softly,
            PageState pageState,
            Check check,
            Object expected,
            Object actual
    ) {
        softly.assertThat(actual.toString())
                .withFailMessage(buildFailMessage(pageState, check, expected, actual))
                .matches(expected.toString());
    }

    private static String buildFailMessage(PageState pageState, Check check, Object expected, Object actual) {
        return ErrorMessages.buildErrorMessage(check.context(pageState), check.failMessage(expected, actual));
    }
}