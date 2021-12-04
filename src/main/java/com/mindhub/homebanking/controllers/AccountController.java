package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createNewAccounts( Authentication authentication, @RequestParam String accountType) {

        Client clientAutenticado = clientRepository.findByEmail(authentication.getName());
        if (authentication.getAuthorities().iterator().next().toString().contains("CLIENT")) {
            if (clientAutenticado.getAccounts().stream().filter(account -> !account.isAccountDelete()).collect(Collectors.toSet()).size() < 3) {
                Random numberRandom = new Random();
                if(accountType.equals("CHECKING")){
                    accountRepository.save(new Account(clientAutenticado, "VIN" + "-" + String.format("%07d", numberRandom.nextInt(1000000)), LocalDateTime.now(), 0.0, false, AccountType.CHECKING));
                } else {
                    accountRepository.save(new Account(clientAutenticado, "VIN" + "-" + String.format("%07d", numberRandom.nextInt(1000000)), LocalDateTime.now(), 0.0, false, AccountType.SAVINGS));
                }
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("**Max Accounts**", HttpStatus.FORBIDDEN);
            } // llave maximo de cuentas 3
         } else {
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/clients/delete/account/{number}")
        public ResponseEntity<Object> deleteAccount(@PathVariable String number){

        Account deleteAccount = accountRepository.findByNumber(number);
        if (deleteAccount.getNumber() == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN); //Verificar que exista la cuenta de origen
        }
        if(deleteAccount.getBalance() > 0){
            return new ResponseEntity<>("Account must be at zero", HttpStatus.FORBIDDEN); //La cuenta de estar en 0
        }
        deleteAccount.setAccountDelete(true);
        accountRepository.save(deleteAccount);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
