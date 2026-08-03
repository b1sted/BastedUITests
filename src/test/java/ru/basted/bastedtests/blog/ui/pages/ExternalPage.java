package ru.basted.bastedtests.blog.ui.pages;

import org.openqa.selenium.WebDriver;

import ru.basted.bastedtests.annotations.PageInfo;
import ru.basted.bastedtests.base.AbstractPageCore;

@PageInfo(siteEntity = "Внешний ресурс", name = "Внешняя страница")
public final class ExternalPage extends AbstractPageCore<ExternalPage> {
    public ExternalPage(WebDriver webDriver) {
        super(webDriver);
    }
}
