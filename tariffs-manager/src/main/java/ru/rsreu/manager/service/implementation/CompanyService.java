package ru.rsreu.manager.service.implementation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsreu.manager.domain.Company;
import ru.rsreu.manager.domain.repository.CompanyRepository;
import ru.rsreu.manager.service.EntityService;

@Service
@Transactional
public class CompanyService implements EntityService<Company> {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company findById(long id) {
        return companyRepository.findById(id);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public Company update(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company add(Company company) {
        return companyRepository.save(company);
    }

    public Company getByName(String name) {
        return companyRepository.getByName(name);
    }

    public boolean isUnique(Company company) {
        Company foundCompany = companyRepository.getByName(company.getName());
        // Либо такого имени нет у других компаний,
        // либо компания с таким именем найдена и это
        // и есть редактируемая компания
        return foundCompany == null || (foundCompany.getId().equals(company.getId()));
    }
}
