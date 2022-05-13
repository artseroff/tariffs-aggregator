package ru.rsreu.serov.tariffs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsreu.serov.tariffs.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}