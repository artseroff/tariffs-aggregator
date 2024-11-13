package ru.rsreu.megafon.service.fetcher;

import java.util.List;
import ru.rsreu.megafon.dto.Tariff;

public interface TariffsFetcher {
    List<Tariff> getActualTariffs();
}
