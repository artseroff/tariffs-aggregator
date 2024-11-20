package ru.rsreu.manager.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    @EntityGraph(
        type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = "company"
    )
    @Override
    List<Tariff> findAll();

    List<Tariff> findAllByCompanyAndAddedNotByUser(Company company, boolean addedNotByUser);

    Tariff findByNameAndCompanyIdAndAddedNotByUser(
        String name,
        Long companyId,
        boolean addedNotByUser
    );

    List<Tariff> findAllByCompanyId(long id);
}
