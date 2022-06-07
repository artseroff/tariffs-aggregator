package ru.rsreu.serov.tariffs.service.implementation;

import org.springframework.stereotype.Service;
import org.w3c.dom.Entity;
import ru.rsreu.serov.tariffs.entity.Tariff;
import ru.rsreu.serov.tariffs.repository.TariffRepository;
import ru.rsreu.serov.tariffs.service.EntityService;

import java.util.List;
import java.util.Objects;

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

