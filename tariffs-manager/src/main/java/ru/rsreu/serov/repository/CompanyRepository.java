package ru.rsreu.serov.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.serov.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findById(long id);

    Company getByName(String name);

    List<Company> findAll();
}
