package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.CompanyMapper;
import com.laborsoftware.xpense.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CompanyController {

    public final CompanyService companyService;

    @Autowired
    public final CompanyMapper companyMapper;

    public CompanyController(
            CompanyService companyService,
            CompanyMapper companyMapper
    ) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @PostMapping("/companies")
    ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        try {
            Company company = companyMapper.toEntity(companyDTO);
            company = companyService.save(company);
            CompanyDTO result = companyMapper.toDto(company);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/companies/{id}")
    ResponseEntity<CompanyDTO> updateCompany(@RequestBody CompanyDTO companyDTO, @PathVariable Long id) {
        try {
            Optional<Company> optionalEvent = companyService.findOne(id);
            if(optionalEvent.isPresent()) {
                Company company = companyMapper.toEntity(companyDTO);
                company = companyService.save(company);
                CompanyDTO result = companyMapper.toDto(company);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/companies")
    ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<Company> companies = companyService.findAll();
        List<CompanyDTO> result = companies.stream().map(companyMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/companies/{id}")
    ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        try {
            Optional<Company> optionalEvent = companyService.findOne(id);
            if(optionalEvent.isPresent()) {
                optionalEvent.get();
                Company company = optionalEvent.get();
                CompanyDTO result = companyMapper.toDto(company);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/companies/{id}")
    ResponseEntity<CompanyDTO> deleteCompany(@PathVariable Long id) {
        try {
            Company companyToDelete = companyService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            companyService.delete(id);
            CompanyDTO result = companyMapper.toDto(companyToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
