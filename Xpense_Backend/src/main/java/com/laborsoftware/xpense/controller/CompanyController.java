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
    ResponseEntity<Company> createEvent(@RequestBody CompanyDTO companyDTO) {
        try {
            Company company = companyMapper.toEntity(companyDTO);
            return ResponseEntity.ok().body(companyService.save(company));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/companies/{id}")
    ResponseEntity<Company> updateEvent(@RequestBody CompanyDTO companyDTO, @PathVariable Long id) {
        try {
            Optional<Company> optionalEvent = companyService.findOne(id);
            if(optionalEvent.isPresent()) {
                return ResponseEntity.ok().body(companyService.save(companyMapper.toEntity(companyDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/companies")
    ResponseEntity<List<Company>> getAllEvents() {
        return new ResponseEntity<>(companyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/companies/{id}")
    ResponseEntity<Company> getEvent(@PathVariable Long id) {
        try {
            Optional<Company> optionalEvent = companyService.findOne(id);
            if(optionalEvent.isPresent()) {
                optionalEvent.get();
                return ResponseEntity.ok().body(optionalEvent.get());
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
    ResponseEntity<Company> deleteEvent(@PathVariable Long id) {
        try {
            Company companyToDelete = companyService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            companyService.delete(id);
            return new ResponseEntity<>(companyToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
