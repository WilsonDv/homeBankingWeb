package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CardsController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createNewCards(Authentication authentication, @RequestParam String type,  @RequestParam String color, @RequestParam String accountNumber) {

        Client clientAutenticado = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(accountNumber);

        if (authentication.getAuthorities().iterator().next().toString().contains("CLIENT")) {
            if(!clientAutenticado.getAccounts().contains(account)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }
            if(clientAutenticado.getCards().stream().filter(card-> !card.isCardDelete()).collect(Collectors.toSet()).size() < 6) {

                Random cardNumberRandom = new Random();
                Set<Card> creditCards = clientAutenticado.getCards().stream().filter( card -> card.getType().toString().contains("CREDIT")).collect(Collectors.toSet());
                Set<Card> debitCards = clientAutenticado.getCards().stream().filter( card -> card.getType().toString().contains("DEBIT")).collect(Collectors.toSet());

                if (type.equals("DEBIT")) {

                    if(debitCards.stream().filter(card-> !card.isCardDelete()).collect(Collectors.toSet()).size() < 3 ){

                        if(color.equals("GOLD")) {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.DEBIT, CardColor.GOLD, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) , String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        } else if (color.equals("SILVER")) {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.DEBIT, CardColor.SILVER, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        } else {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.DEBIT, CardColor.TITANIUM, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        }

                    }else{
                        return new ResponseEntity<>("You already have the maximum debit card", HttpStatus.EXPECTATION_FAILED);
                    }

                } //typeDebit
                else {

                    if (creditCards.stream().filter(card-> !card.isCardDelete()).collect(Collectors.toSet()).size() < 3 ) {

                        if (color.equals("GOLD")) {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.CREDIT, CardColor.GOLD, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        } else if (color.equals("SILVER")) {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.CREDIT, CardColor.SILVER, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        } else {
                            cardRepository.save(new Card(clientAutenticado, account, CardType.CREDIT, CardColor.TITANIUM, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000) + 1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false));
                            return new ResponseEntity<>(HttpStatus.CREATED);
                        }

                    } else{
                        return new ResponseEntity<>("You already have the maximum credit card", HttpStatus.EXPECTATION_FAILED);
                    }
                }
            } // menor a 6
        }
        return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
    }
    @DeleteMapping("/api/clients/delete/cards/{cvv}")
    public ResponseEntity<Object> deleteCard(@PathVariable String cvv){
        Card cardDelete = cardRepository.findByCvv(cvv);
        if(cardDelete == null){
            return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
        }
            cardDelete.setCardDelete(true);
            cardRepository.save(cardDelete);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}//CardsController
