package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardPaymentsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
public class CardPaymentsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Transactional
    @PostMapping("/api/cardsPayments")
    public ResponseEntity<Object> createNewCardPayments( @RequestBody CardPaymentsDTO cardPaymentsDTO, @RequestParam String accountNumber) {

        Card verifyNumberCard = cardRepository.findByCvv(cardPaymentsDTO.getCvv());
        Account verifyAccount = accountRepository.findByNumber(accountNumber);
        LocalDateTime today = LocalDateTime.now();

        if ( cardPaymentsDTO.getAmount() <= 0 || cardPaymentsDTO.getDescription().isEmpty() || cardPaymentsDTO.getNumber().isEmpty() || cardPaymentsDTO.getCvv().isEmpty() )  {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (verifyNumberCard.getNumber() == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if ( verifyNumberCard.getAccount().getBalance() <  cardPaymentsDTO.getAmount()){
            return new ResponseEntity<>("Accounts do not have enough balance", HttpStatus.FORBIDDEN); //Verificar que la cuenta de origen tenga el monto disponible.
        }

        if(verifyNumberCard.getThruDate().isBefore(today)  ) {
            return new ResponseEntity<>("Expired card", HttpStatus.FORBIDDEN);
        }
        double newBalanceOrigen = verifyNumberCard.getAccount().getBalance() - cardPaymentsDTO.getAmount();

        verifyNumberCard.getAccount().setBalance(newBalanceOrigen);

        Transaction transactionDebit  =  new Transaction(verifyNumberCard.getAccount(), TransactionType.DEBIT, cardPaymentsDTO.getAmount(), cardPaymentsDTO.getDescription(), LocalDateTime.now(), newBalanceOrigen);
        transactionRepository.save(transactionDebit);

        accountRepository.save(verifyNumberCard.getAccount());

        double newBalance = verifyAccount.getBalance() + cardPaymentsDTO.getAmount();

        verifyAccount.setBalance(newBalance);

        Transaction transactionCredit =  new Transaction(verifyAccount, TransactionType.CREDIT, cardPaymentsDTO.getAmount(), cardPaymentsDTO.getDescription(), LocalDateTime.now(), newBalance);
        transactionRepository.save(transactionCredit);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }//ResponseEntity

}



