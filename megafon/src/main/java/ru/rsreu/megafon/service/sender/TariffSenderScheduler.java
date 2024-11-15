package ru.rsreu.megafon.service.sender;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.rsreu.megafon.dto.Tariff;
import ru.rsreu.megafon.service.fetcher.TariffsFetcher;
import ru.rsreu.megafon.service.sender.dto.TariffsData;

@Slf4j
@Component
@ConditionalOnProperty(value = "scheduler.enable")
public class TariffSenderScheduler {

    private final String companyName;
    private final TariffsFetcher tariffsFetcher;
    private final SenderService senderService;

    public TariffSenderScheduler(
        @Value("${megafon.name}") String companyName,
        TariffsFetcher tariffsFetcher,
        SenderService senderService
    ) {
        this.companyName = companyName;
        this.tariffsFetcher = tariffsFetcher;
        this.senderService = senderService;
    }

    @Scheduled(fixedDelayString = "${scheduler.interval}")
    public void update() {

        List<Tariff> tariffs = tariffsFetcher.getActualTariffs();
        if (tariffs.isEmpty()) {
            return;
        }
        TariffsData message = new TariffsData(companyName, tariffs);
        senderService.send(message);
        log.info("Отправлено {} актуальных тарифов", tariffs.size());

    }
}
