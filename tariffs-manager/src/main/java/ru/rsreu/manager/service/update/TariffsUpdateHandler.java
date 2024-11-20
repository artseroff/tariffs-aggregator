package ru.rsreu.manager.service.update;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.dto.TariffDto;
import ru.rsreu.manager.dto.TariffsData;
import ru.rsreu.manager.service.implementation.TariffService;

@AllArgsConstructor
@Service
public class TariffsUpdateHandler {
    private final TariffService tariffService;

    public void processUpdate(TariffsData tariffsData) {
        List<TariffDto> tariffDtos = tariffsData.tariffs();

        List<Tariff> tariffEntities = new ArrayList<>();
        for (TariffDto tariffDto : tariffDtos) {
            Tariff tariff = TariffsDtoToEntityMapper.mapToTariffEntity(tariffDto);
            tariff.setAddedNotByUser(true);
            tariffEntities.add(tariff);
        }

        tariffService.updateScrappedTariffs(tariffEntities, tariffsData.company());
    }
}
