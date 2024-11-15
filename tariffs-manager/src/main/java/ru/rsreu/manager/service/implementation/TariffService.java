package ru.rsreu.manager.service.implementation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.domain.repository.TariffRepository;
import ru.rsreu.manager.service.EntityService;

@Service
@Transactional
public class TariffService implements EntityService<Tariff> {

    private final TariffRepository tariffRepository;

    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Tariff findById(long id) {
        return tariffRepository.findById(id);
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
    public Tariff add(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    public boolean isUnique(Tariff tariff) {
        Tariff foundTariff = tariffRepository.findByNameAndCompanyIdAndAddedNotByUser(tariff.getName(),
            tariff.getCompany().getId(),
            tariff.isAddedNotByUser()
        );
        // Либо такого имени нет у других тарифов,
        // либо тариф с таким именем найден и это
        // и есть редактируемый тариф
        return foundTariff == null || (foundTariff.getId().equals(tariff.getId()));
    }

    public void updateScrappedTariffs(List<Tariff> newTariffs) {
        if (newTariffs.isEmpty()) {
            return;
        }

        Company company = newTariffs.get(0).getCompany();
        List<Tariff> savedTariffs = tariffRepository.findAllByCompanyAndAddedNotByUser(company, true);

        Map<String, Tariff> mapNameSavedTariff = new HashMap<>();
        for (Tariff tariff : savedTariffs) {
            mapNameSavedTariff.put(tariff.getName(), tariff);
        }
        removeNotUpdatedTariffOrSetIdInList(newTariffs, mapNameSavedTariff);

        tariffRepository.saveAll(newTariffs);

    }

    private static void removeNotUpdatedTariffOrSetIdInList(
        List<Tariff> newTariffs,
        Map<String, Tariff> mapNameTariff
    ) {
        if (mapNameTariff.isEmpty()) {
            return;
        }
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
        }
    }
}

