package ru.rsreu.megafon.service.fetcher.html;

import java.net.URI;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class MegafonClient {

    private static final String ALL_TARIFFS_ENDPOINT = "/tariffs/all/";
    private final URI allTariffsUrl;
    private final String baseUrl;

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    public MegafonClient(
        @Value("${client.megafon.base-url}") String baseUrl,
        RestTemplate restTemplate,
        RetryTemplate retryTemplate
    ) {
        this.baseUrl = baseUrl;
        this.allTariffsUrl = HtmlParserUtils.combineUrl(baseUrl, ALL_TARIFFS_ENDPOINT);

        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    String getBaseUrl() {
        return baseUrl;
    }

    public Optional<String> getHtmlBodyAllTariffs() {
        return getHtmlBody(allTariffsUrl);
    }

    public Optional<String> getHtmlBodyTariffLink(URI url) {
        return getHtmlBody(url);
    }

    private Optional<String> getHtmlBody(URI url) {
        Optional<String> optionalEmpty = Optional.empty();
        try {
            ResponseEntity<String> entity = retryTemplate.execute(context ->
                restTemplate.getForEntity(url, String.class)
            );

            String body = entity.getBody();
            if (entity.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(body);
            } else {
                return optionalEmpty;
            }

        } catch (RestClientException e) {
            log.error(e.getMessage());
            return optionalEmpty;
        }
    }
}
