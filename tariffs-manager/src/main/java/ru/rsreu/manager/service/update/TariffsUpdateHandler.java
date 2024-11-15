package ru.rsreu.manager.service.update;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.dto.TariffDto;
import ru.rsreu.manager.dto.TariffsData;
import ru.rsreu.manager.service.implementation.CompanyService;
import ru.rsreu.manager.service.implementation.TariffService;

@AllArgsConstructor
@Service
public class TariffsUpdateHandler {
    private final CompanyService companyService;
    private final TariffService tariffService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processUpdate(TariffsData tariffsData) {
        List<TariffDto> tariffDtos = tariffsData.tariffs();

        Company company = companyService.getByName(tariffsData.company());

        List<Tariff> tariffEntities = new ArrayList<>();
        for (TariffDto tariffDto : tariffDtos) {
            Tariff tariff = TariffsDtoToEntityMapper.mapToTariffEntity(tariffDto, company);
            tariff.setAddedNotByUser(true);
            tariffEntities.add(tariff);
        }

        tariffService.updateScrappedTariffs(tariffEntities);
    }
}
