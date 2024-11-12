package ru.rsreu.serov.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rsreu.serov.entity.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    Tariff findById(long id);

    Tariff getByName(String name);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = "company"
    )
    List<Tariff> findAll();
}
