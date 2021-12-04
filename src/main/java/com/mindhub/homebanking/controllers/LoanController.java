package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;


    @GetMapping(value = "/api/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/api/clients/current/loans")
    public ResponseEntity<Object> createNewLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanSolicitud) {

        Client  clientAutenticado = clientRepository.findByEmail(authentication.getName());
        Account destinationAccountLoan = accountRepository.findByNumber(loanSolicitud.getDestinationAccount());
        Loan    loanRequested = loanRepository.findByName(loanSolicitud.getName());

        Set<Account> numberAccounts = clientAutenticado.getAccounts().stream().filter(numberAccount -> numberAccount.getNumber().contains(loanSolicitud.getDestinationAccount())).collect(Collectors.toSet());

        if (loanSolicitud.getName().isEmpty() ||loanSolicitud.getPayments().toString().isEmpty() || loanSolicitud.getDestinationAccount().isEmpty() || String.valueOf(loanSolicitud.getAmount()).isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //Verificar que los parámetros no estén vacíos
        }

        if (loanSolicitud.getAmount() <= 0 || loanSolicitud.getAmount() > loanRequested.getMaxAmount()) {
            return new ResponseEntity<>("Amount not accepted", HttpStatus.FORBIDDEN); //monto menor a 0 y mayor al  disponible
        }

        if (loanRequested == null) {
            return new ResponseEntity<>("Loan does not exist", HttpStatus.FORBIDDEN); //Verificar que el prestamo  existe
        }

        if (destinationAccountLoan == null) {
            return new ResponseEntity<>("Account does not exist", HttpStatus.FORBIDDEN); //Verifica si la cuenta no existe
        }
        if(numberAccounts.isEmpty()) {
            return new ResponseEntity<>("The account does not belong to you", HttpStatus.FORBIDDEN); //Verificar que la cuenta de destino pertenezca al cliente autenticado
        }

        destinationAccountLoan.setBalance(destinationAccountLoan.getBalance() + loanSolicitud.getAmount());

        double percentageLoan = loanRequested.getPercentage();

        ClientLoan clientLoan = new ClientLoan(loanSolicitud.getAmount() * percentageLoan, loanSolicitud.getPayments());
        Transaction transactionCredit  =  new Transaction(destinationAccountLoan, TransactionType.CREDIT, loanSolicitud.getAmount(), loanSolicitud.getName() +" "+"loan approved", LocalDateTime.now(), destinationAccountLoan.getBalance());

        clientAutenticado.addClientLoan(clientLoan);
        loanRequested.addClientLoan(clientLoan);

        clientRepository.save(clientAutenticado);
        clientLoanRepository.save(clientLoan);
        transactionRepository.save(transactionCredit);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
