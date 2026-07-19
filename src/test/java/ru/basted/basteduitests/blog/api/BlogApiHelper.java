package ru.basted.basteduitests.blog.api;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jspecify.annotations.NonNull;

public class BlogApiHelper {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Выполняет GET-запрос к файлу sitemap.xml.
     *
     * @param sitemapUrl URL-адрес файла sitemap.xml
     * @return HTTP-ответ, где тело (body) представлено в виде {@link InputStream}.
     *         Поток требует обязательного закрытия для освобождения сетевых ресурсов.
     */
    public HttpResponse<InputStream> getSitemapStream(String sitemapUrl) {
        return sendGetRequest(sitemapUrl);
    }

    /**
     * Извлекает из потока с телом sitemap.xml ссылки страниц сайта.
     *
     * @param response HTTP-ответ, где тело (body) представлено в виде {@link InputStream}
     * @return Список абсолютных ссылок сайта
     */
    public Set<String> extractUrlsFromSitemap(@NonNull HttpResponse<InputStream> response) {
        try (InputStream xmlStream = response.body()) {
            return parseLocUrls(xmlStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выполняет последовательный обход списка URL-адресов методом HTTP HEAD, собирая
     * карту ответов для последующей валидации.
     *
     * @param urls список уникальных абсолютных ссылок сайта
     * @return Карту результатов в формате "Ссылка -> Http статус-код"
     */
    public Map<String, Integer> checkUrlsStatuses(@NonNull Set<String> urls) {
        Map<String, Integer> urlsStatuses = new HashMap<>();

        for (String url : urls) {
            int responseCode = sendHeadRequest(url).statusCode();
            urlsStatuses.put(url, responseCode);
        }

        return urlsStatuses;
    }

    private Set<String> parseLocUrls(InputStream xmlStream) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        XMLStreamReader reader = factory.createXMLStreamReader(xmlStream);
        try {
            Set<String> urls = new HashSet<>();

            while (reader.hasNext()) {
                int eventType = reader.next();

                if (eventType == XMLStreamConstants.START_ELEMENT && "loc".equals(reader.getLocalName())) {
                    urls.add(reader.getElementText());
                }
            }

            return urls;
        } finally {
            reader.close();
        }
    }

    private HttpResponse<InputStream> sendGetRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return sendRequest(request);
    }

    private HttpResponse<InputStream> sendHeadRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .HEAD()
                .build();

        return sendRequest(request);
    }

    private HttpResponse<InputStream> sendRequest(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (Exception e) {
            throw new RuntimeException("HTTP запрос завершился ошибкой", e);
        }
    }
}
