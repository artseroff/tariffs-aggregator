package ru.rsreu.megafon.service.fetcher;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import ru.rsreu.megafon.dto.Tariff;

public interface TariffsFetcher {
    List<Tariff> getActualTariffs();

    Optional<Tariff> getTariffByUrl(URI url);
}
