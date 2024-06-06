package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.CompanyMapper;
import com.laborsoftware.xpense.repository.CompanyRepository;
import com.laborsoftware.xpense.service.crud.ICrudService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RestController
@RequestMapping
public class CompanyService implements ICrudService<CompanyDTO, Long> {
    private final CompanyRepository companyRepository;

    @Autowired
    public final CompanyMapper companyMapper;

    public CompanyService(
            CompanyRepository companyRepository,
            CompanyMapper companyMapper
    ) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    @PostMapping("/companies")
    public ResponseEntity<CompanyDTO> save(@RequestHeader("Authorization") String token,@RequestBody CompanyDTO companyDTO) {
        logger.debug("Request to save Company {} ", companyDTO);
        try {
            Company company = companyMapper.toEntity(companyDTO);
            company = companyRepository.save(company);
            CompanyDTO result = companyMapper.toDto(company);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> update(@RequestHeader("Authorization") String token,@RequestBody CompanyDTO companyDTO, @PathVariable Long id) {
        try {
            Optional<Company> optionalEvent = companyRepository.findById(id);
            if(optionalEvent.isPresent()) {
                Company company = companyMapper.toEntity(companyDTO);
                company = companyRepository.save(company);
                CompanyDTO result = companyMapper.toDto(company);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/companies/{id}")
    public void delete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        logger.debug("Request to delete ");
        try {
            Optional<Company> companyToDelete = companyRepository.findById(id);
            if (companyToDelete.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
            companyRepository.deleteById(id);
        } catch (Exception ex) {

            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDTO>> findAll(@RequestHeader("Authorization") String token) {
        logger.debug("Request to get all Companies");
        List<Company> companies = companyRepository.findAll();
        List<CompanyDTO> result = companies.stream().map(companyMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @Override
    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> findOne(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            Optional<Company> optionalCompany  = companyRepository.findById(id);
            if(optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                CompanyDTO result = companyMapper.toDto(company);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
