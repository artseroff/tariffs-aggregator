package ru.rsreu.megafon.service.fetcher.html;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.rsreu.megafon.dto.Tariff;
import ru.rsreu.megafon.service.fetcher.TariffsFetcher;

@Component
public class HtmlTariffsParser implements TariffsFetcher {
    private static final String RUB_ENDING = " ₽";
    private static final String GB_ENDING = " ГБ";
    private static final String MINUTES_ENDING = " минут";
    private static final String SMS_ENDING = " SMS";
    private static final String RUB_PER_MINUTE_ENDING = " ₽ за минуту";
    private static final String RUB_PER_MESSAGE_ENDING = " ₽ за сообщение";
    private static final String PER_MINUTE_TITLE = "Звонки на номера других операторов региона пребывания";
    private static final String PER_MESSAGE_TITLE = "SMS на номера региона пребывания";
    private static final String PER_MINUTE_MESSAGE_SELECTOR_FORMAT =
        "div[data-gtm-tariff-param-title='%s'] > div:nth-child(1) > div.tariffs-detail-row-v3__right-column > h3";

    private final MegafonClient megafonClient;

    public HtmlTariffsParser(MegafonClient megafonClient) {
        this.megafonClient = megafonClient;
    }

    @Override
    public List<Tariff> getActualTariffs() {
        List<URI> links = getTariffsUrls();

        List<Tariff> tariffs = new ArrayList<>();

        for (URI link : links) {
            Optional<Tariff> optionalTariff = getTariffByUrl(link);
            optionalTariff.ifPresent(tariffs::add);
        }
        return tariffs;
    }

    private List<URI> getTariffsUrls() {
        List<URI> uriList = Collections.emptyList();

        Optional<String> optionalHtmlBody = megafonClient.getHtmlBodyAllTariffs();
        if (optionalHtmlBody.isEmpty()) {
            return uriList;
        }

        String htmlBody = optionalHtmlBody.get();
        Document document = Jsoup.parse(htmlBody);
        Elements tariffCards = document.select("div.tariffs-card-header-v4");
        if (tariffCards.isEmpty()) {
            return uriList;
        }

        Elements aTags = tariffCards.select("a.tariffs-card-header-v4__title-link");
        if (aTags.isEmpty()) {
            return uriList;
        }

        uriList = new ArrayList<>();
        for (Element aTag : aTags) {
            String link = aTag.attr("href");
            if (link != null && !link.isBlank()) {
                URI url = HtmlParserUtils.combineUrl(megafonClient.getBaseUrl(), link);
                uriList.add(url);
            }
        }
        return uriList;
    }

    @Override
    public Optional<Tariff> getTariffByUrl(URI url) {
        Optional<Tariff> optionalEmpty = Optional.empty();

        Optional<String> optionalBody = megafonClient.getHtmlBodyTariffLink(url);
        if (optionalBody.isEmpty()) {
            return optionalEmpty;
        }
        String body = optionalBody.get();
        Document document = Jsoup.parse(body);

        Tariff.TariffBuilder tariffBuilder = Tariff.builder();

        if (!tryFillNamePriceFields(document, tariffBuilder)) {
            return optionalEmpty;
        }
        fillServicePackageFields(document, tariffBuilder);
        fillPricesPerMinuteMessageFields(document, tariffBuilder);

        tariffBuilder.url(url);
        return Optional.of(tariffBuilder.build());
    }

    private boolean tryFillNamePriceFields(Document document, Tariff.TariffBuilder tariffBuilder) {
        Elements shortPageDetails = document.select("div[data-gtm-tariff-id][data-gtm-tariff-title]");
        String title = shortPageDetails.attr("data-gtm-tariff-title");
        if (title.isBlank()) {
            return false;
        }
        tariffBuilder.name(title);
        String priceText = shortPageDetails.attr("data-gtm-tariff-amount");
        if (priceText.isBlank()) {
            return false;
        }
        int price = Integer.parseInt(HtmlParserUtils.cutEnding(priceText, RUB_ENDING));
        tariffBuilder.amount(price);
        return true;
    }

    private void fillServicePackageFields(Document document, Tariff.TariffBuilder tariffBuilder) {
        Elements shortDetails = document.select("div.tariffs-detail-short-base-item");
        if (shortDetails.isEmpty()) {
            return;
        }
        Elements detailsValues = shortDetails.select("div.tariffs-detail-short-base-item__value");

        for (Element detailValue : detailsValues) {
            if (!detailsValues.hasText()) {
                continue;
            }

            String currentField = detailValue.text();
            if (currentField.endsWith(GB_ENDING)) {
                int countGB = Integer.parseInt(HtmlParserUtils.cutEnding(currentField, GB_ENDING));
                tariffBuilder.countGBInternetTraffic(countGB);
            } else if (currentField.endsWith(MINUTES_ENDING)) {
                int countFreeMinutes = Integer.parseInt(HtmlParserUtils.cutEnding(currentField, MINUTES_ENDING));
                tariffBuilder.countFreeMinutes(countFreeMinutes);
            } else if (currentField.endsWith(SMS_ENDING)) {
                int countFreeMessages = Integer.parseInt(HtmlParserUtils.cutEnding(currentField, SMS_ENDING));
                tariffBuilder.countFreeMessages(countFreeMessages);
            }
        }
    }

    private void fillPricesPerMinuteMessageFields(Document document, Tariff.TariffBuilder tariffBuilder) {
        Elements h3PricePerMinute = document.select(PER_MINUTE_MESSAGE_SELECTOR_FORMAT.formatted(PER_MINUTE_TITLE));
        if (h3PricePerMinute.hasText()) {
            String pricePerMinuteText = h3PricePerMinute.text();
            float pricePerMinute =
                Float.parseFloat(HtmlParserUtils.cutEnding(pricePerMinuteText, RUB_PER_MINUTE_ENDING));
            tariffBuilder.costPerMinute(pricePerMinute);
        }

        Elements h3PricePerMessage = document.select(PER_MINUTE_MESSAGE_SELECTOR_FORMAT.formatted(PER_MESSAGE_TITLE));
        if (h3PricePerMessage.hasText()) {
            String pricePerMessageText = h3PricePerMessage.text();
            float pricePerMessage =
                Float.parseFloat(HtmlParserUtils.cutEnding(pricePerMessageText, RUB_PER_MESSAGE_ENDING));
            tariffBuilder.costPerMessage(pricePerMessage);
        }
    }
}
