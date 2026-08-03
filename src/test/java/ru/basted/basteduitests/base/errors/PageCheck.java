package ru.basted.basteduitests.base.errors;

public enum PageCheck implements Check {
    URL("%s -> %s -> Параметр: URL", "Ожидался URL '%s', но получен '%s'"),
    TITLE("%s -> %s -> Параметр: Название страницы", "Ожидалось %s, но получено %s");

    private final String context;
    private final String failMessageTemplate;

    PageCheck(String context, String failMessageTemplate) {
        this.context = context;
        this.failMessageTemplate = failMessageTemplate;
    }

    @Override
    public String context(PageState pageState) {
        return context.formatted(pageState.siteEntity(), pageState.pageTitle());
    }

    @Override
    public String failMessage(Object expected, Object actual) {
        return failMessageTemplate.formatted(expected, actual);
    }
}
