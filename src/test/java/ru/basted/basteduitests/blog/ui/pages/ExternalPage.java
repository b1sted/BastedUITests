package ru.basted.basteduitests.blog.ui.pages;

import org.openqa.selenium.WebDriver;

import ru.basted.basteduitests.annotations.PageInfo;
import ru.basted.basteduitests.base.AbstractPageCore;

@PageInfo(siteEntity = "Внешний ресурс", name = "Внешняя страница")
public final class ExternalPage extends AbstractPageCore<ExternalPage> {
    public ExternalPage(WebDriver webDriver) {
        super(webDriver);
    }
}
