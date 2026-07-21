package ru.basted.basteduitests.blog.api;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.basted.basteduitests.config.Configs;

import static org.assertj.core.api.Assertions.assertThat;

public class SitemapTests {
    private final BlogApiHelper apiHelper = new BlogApiHelper();

    @Test
    @DisplayName("Проверка доступности всех URL из sitemap.xml")
    public void sitemapUrlsShouldReturn200() {
        HttpResponse<InputStream> response = apiHelper.getSitemapStream(Configs.project().baseUrl() + "/sitemap.xml");
        assertThat(response.statusCode())
                .as("Статус-код запроса получения sitemap.xml")
                .isEqualTo(200);

        Set<String> urls = apiHelper.extractUrlsFromSitemap(response);
        assertThat(urls)
                .as("Список URL-адресов")
                .isNotEmpty();

        Map<String, Integer> urlStatusMap = apiHelper.checkUrlsStatuses(urls);
        SoftAssertions softly = new SoftAssertions();
        urlStatusMap.forEach((currentUrl, statusCode) ->
                softly.assertThat(statusCode)
                        .as("Доступность URL: %s", currentUrl)
                        .isBetween(200, 299)
        );
        softly.assertAll();
    }
}
