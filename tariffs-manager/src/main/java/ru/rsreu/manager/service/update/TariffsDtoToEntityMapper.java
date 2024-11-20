package ru.rsreu.manager.service.update;

import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.dto.TariffDto;

public final class TariffsDtoToEntityMapper {
    private TariffsDtoToEntityMapper() {
    }

    public static Tariff mapToTariffEntity(TariffDto tariffDto) {
        return Tariff.builder()
            .name(tariffDto.name())
            .countFreeMessages(tariffDto.countFreeMessages())
            .countFreeMinutes(tariffDto.countFreeMinutes())
            .countGBInternetTraffic(tariffDto.countGBInternetTraffic())
            .costPerMessage(tariffDto.costPerMessage())
            .costPerMinute(tariffDto.costPerMinute())
            .amount(tariffDto.amount())
            .url(tariffDto.url())
            .build();

    }
}
