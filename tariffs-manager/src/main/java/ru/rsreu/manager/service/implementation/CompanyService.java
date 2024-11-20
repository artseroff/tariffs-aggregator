package ru.rsreu.manager.service.implementation;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.Tariff;
import ru.rsreu.manager.domain.repository.CompanyRepository;
import ru.rsreu.manager.domain.repository.TariffRepository;
import ru.rsreu.manager.service.EntityService;
import ru.rsreu.manager.service.exception.EntityHasOrphansException;
import ru.rsreu.manager.service.exception.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService implements EntityService<Company> {

    private final TariffRepository tariffRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Company findById(long id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return optionalCompany.orElseThrow(() ->
            new EntityNotFoundException("Компания с id=%d не найдена".formatted(id)));
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        List<Tariff> tariffs = tariffRepository.findAllByCompanyId(id);
        if (!tariffs.isEmpty()) {
            throw new EntityHasOrphansException("Нельзя удалить компанию, у которой есть действующие тарифы");
        }
        companyRepository.deleteById(id);
    }

    @Override
    public Company update(Company company) {
        return companyRepository.save(company);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean processUpdateTransactional(Company company) {
        return processUpdate(company);
    }

    @Override
    public boolean isUnique(Company company) {
        Company foundCompany = companyRepository.getByName(company.getName());
        // Либо такого имени нет у других компаний,
        // либо компания с таким именем найдена и это
        // и есть редактируемая компания
        return foundCompany == null || (foundCompany.getId().equals(company.getId()));
    }

    public Company getByName(String name) {
        return companyRepository.getByName(name);
    }
}
