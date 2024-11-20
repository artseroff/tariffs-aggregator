package ru.rsreu.manager.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.manager.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company getByName(String name);
}
