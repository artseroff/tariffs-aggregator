package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.serov.tariffs.entity.Role;
import ru.rsreu.serov.tariffs.entity.Tariff;

import java.util.List;
import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Tariff findById(long id);
    Tariff getByName(String name);
    List<Tariff> findAll();
}