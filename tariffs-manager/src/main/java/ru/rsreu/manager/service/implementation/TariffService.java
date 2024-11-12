package ru.rsreu.manager.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.rsreu.manager.entity.Tariff;
import ru.rsreu.manager.repository.TariffRepository;
import ru.rsreu.manager.service.EntityService;

@Service
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

    public Tariff getByName(String name) {
        return tariffRepository.getByName(name);
    }

    public boolean isNewNameOfTariffWithIdUnique(String login, Long id) {
        Tariff foundTariff = tariffRepository.getByName(login);
        // Либо такого имени нет у других тарифов,
        // либо тариф с таким именем найден и это
        // и есть редактируемый тариф
        return foundTariff == null || (foundTariff.getId().equals(id));
    }
}

