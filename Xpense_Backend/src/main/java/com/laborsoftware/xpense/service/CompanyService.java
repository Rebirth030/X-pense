package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService implements ICrudService<Company, Long> {

    Logger logger = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    public CompanyService(
            CompanyRepository companyRepository
    ) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company save(Company company) {
        logger.debug("Request to save Company {} ", company);
        Company result = null;
        try {
            result = companyRepository.save(company);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete ");
        try {
            companyRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<Company> findAll() {
        logger.debug("Request to get all Companies");
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findOne(Long id) {
        logger.debug("Request to get all Company");
        return companyRepository.findById(id);
    }
}
