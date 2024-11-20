package ru.rsreu.manager.service.implementation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.domain.repository.TariffRepository;
import ru.rsreu.manager.service.EntityService;
import ru.rsreu.manager.service.exception.EntityNotFoundException;

@Service
@Transactional
public class TariffService implements EntityService<Tariff> {

    private final TariffRepository tariffRepository;
    private final CompanyService companyService;

    public TariffService(TariffRepository tariffRepository, CompanyService companyService) {
        this.tariffRepository = tariffRepository;
        this.companyService = companyService;
    }

    @Override
    public Tariff findById(long id) {
        Optional<Tariff> optionalTariff = tariffRepository.findById(id);
        return optionalTariff.orElseThrow(() ->
            new EntityNotFoundException("Тариф с id=%d не найден".formatted(id)));
    }

    @Override
    public List<Tariff> findAll() {
        return tariffRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        tariffRepository.deleteById(id);
    }

    @Override
    public Tariff update(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    @Override
    public boolean isUnique(Tariff tariff) {
        Tariff foundTariff = tariffRepository.findByNameAndCompanyIdAndAddedNotByUser(
            tariff.getName(),
            tariff.getCompany().getId(),
            tariff.isAddedNotByUser()
        );
        // Либо такого имени нет у других тарифов,
        // либо тариф с таким именем найден и это
        // и есть редактируемый тариф
        return foundTariff == null || (foundTariff.getId().equals(tariff.getId()));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean processUpdateTransactional(Tariff tariff) {
        return processUpdate(tariff);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateScrappedTariffs(List<Tariff> newTariffs, String companyName) {
        Company company = companyService.findByNameIgnoreCase(companyName.toLowerCase());
        if (company == null) {
            company = saveCompanyWithName(companyName);
        }

        if (newTariffs.isEmpty()) {
            return;
        }

        List<Tariff> savedTariffs = tariffRepository.findAllByCompanyAndAddedNotByUser(company, true);

        Map<String, Tariff> mapNameSavedTariff = new HashMap<>();
        for (Tariff tariff : savedTariffs) {
            mapNameSavedTariff.put(tariff.getName(), tariff);
        }
        removeNotUpdatedTariffOrSetSavedIdAndCompanyInList(newTariffs, mapNameSavedTariff, company);

        tariffRepository.saveAll(newTariffs);

    }

    private static void removeNotUpdatedTariffOrSetSavedIdAndCompanyInList(
        List<Tariff> newTariffs,
        Map<String, Tariff> mapNameTariff,
        Company company
    ) {
        Iterator<Tariff> newTariffsIterator = newTariffs.iterator();
        Tariff newTariff;
        while (newTariffsIterator.hasNext()) {
            newTariff = newTariffsIterator.next();
            if (mapNameTariff.containsKey(newTariff.getName())) {
                Tariff savedTariff = mapNameTariff.get(newTariff.getName());

                if (savedTariff.idOptionalEquals(newTariff, true)) {
                    newTariffsIterator.remove();
                } else {
                    newTariff.setId(savedTariff.getId());
                }
            }
            newTariff.setCompany(company);
        }
    }

    private Company saveCompanyWithName(String companyName) {
        Company newCompany = new Company();
        newCompany.setName(companyName);
        return companyService.update(newCompany);
    }
}

