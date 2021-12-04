package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanAdminDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;


@RestController
public class LoanAdminController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoanRepository loanRepository;


    @Transactional
    @PostMapping("/api/admin/loan")
    public ResponseEntity<Object> createNew(Authentication authentication, @RequestBody LoanAdminDTO loanAdmin) {

        Client clientAutenticado = clientRepository.findByEmail(authentication.getName());

        if(loanAdmin.getName().isEmpty() || loanAdmin.getMaxAmount() <= 0 || loanAdmin.getPayments().isEmpty() || loanAdmin.getPercentage() <= 0){
            return new ResponseEntity<>( "Missing data",HttpStatus.FORBIDDEN);
        }
        Loan newLoan = new Loan(loanAdmin.getName(), loanAdmin.getMaxAmount(), loanAdmin.getPayments(), loanAdmin.getPercentage());

        loanRepository.save(newLoan);
        return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
    }
}
