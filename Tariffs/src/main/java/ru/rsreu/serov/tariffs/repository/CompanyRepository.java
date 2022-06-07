package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.serov.tariffs.entity.Company;
import ru.rsreu.serov.tariffs.entity.Tariff;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findById(long id);
    Company getByName(String name);
    List<Company> findAll();
}