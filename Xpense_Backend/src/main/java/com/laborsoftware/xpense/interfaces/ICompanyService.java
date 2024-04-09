package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.Company;

import java.util.List;
import java.util.Optional;

public interface ICompanyService {
    Company save(Company company);

    void delete(Long id);

    List<Company> findAll();

    Optional<Company> findOne(Long id);
}
