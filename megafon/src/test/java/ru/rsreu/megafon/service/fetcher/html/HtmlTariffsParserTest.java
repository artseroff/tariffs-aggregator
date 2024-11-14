package ru.rsreu.megafon.service.fetcher.html;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;
import ru.rsreu.megafon.dto.Tariff;
import ru.rsreu.megafon.service.fetcher.TariffsFetcher;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class HtmlTariffsParserTest {

    private WireMockServer wireMockServer;
    private TariffsFetcher tariffsFetcher;
    private MegafonClient megafonClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        megafonClient =
            new MegafonClient(wireMockServer.baseUrl(), new RestTemplate(), RetryTemplate.defaultInstance());
        tariffsFetcher = new HtmlTariffsParser(megafonClient);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getTariffByUrl_OkResponse() throws IOException {

        // Arrange
        String responseBody = Files.readString(Path.of("src/test/resources/html/minimum.html"), StandardCharsets.UTF_8);

        String endpoint = "/tariffs/all/mf_minimum.html";
        stubFor(get(urlEqualTo(endpoint))
            .willReturn(aResponse().withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8")
                .withBody(responseBody)));

        URI url = HtmlParserUtils.combineUrl(megafonClient.getBaseUrl(), endpoint);

        Tariff expectedTariff = Tariff.builder()
            .name("Минимум")
            .countFreeMessages(30)
            .countFreeMinutes(400)
            .countGBInternetTraffic(6)
            .costPerMinute(3)
            .costPerMessage(2.2f)
            .unlimitedTraffic(false)
            .url(url)
            .amount(650)
            .build();

        // Act
        Optional<Tariff> optionalTariff = tariffsFetcher.getTariffByUrl(url);

        // Assert
        Assertions.assertTrue(optionalTariff.isPresent());
        Assertions.assertEquals(expectedTariff, optionalTariff.get());
    }

    @Test
    public void getTariffByUrl_ErrorResponse() throws IOException {

        // Arrange
        String responseBody = " ";
        String endpoint = "/tariffs/all/mf_minimum.html";
        stubFor(get(urlEqualTo(endpoint))
            .willReturn(aResponse().withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8")
                .withBody(responseBody)));

        URI url = HtmlParserUtils.combineUrl(megafonClient.getBaseUrl(), endpoint);

        // Act
        Optional<Tariff> optionalTariff = tariffsFetcher.getTariffByUrl(url);

        // Assert
        Assertions.assertTrue(optionalTariff.isEmpty());
    }
}
