package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mindhub.homebanking.repositories.ClientRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
public class ClientController {

        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private AccountRepository accountRepository;

        @PostMapping("/api/clients")
        public ResponseEntity<Object> register(

                @RequestParam String firstName, @RequestParam String lastName,
                @RequestParam String email, @RequestParam String password, @RequestParam String accountType) {
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || accountType.isEmpty()) {

                        return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
                }
                if (clientRepository.findByEmail(email) !=  null) {

                        return new ResponseEntity<>("email already exist", HttpStatus.FORBIDDEN);
                }

                Random numberRandom = new Random();
                Client clientRegistration =  new Client(firstName, lastName, email, passwordEncoder.encode(password));
                Account accountNew;

                if(accountType.equals("SAVINGS")){
                        accountNew = new Account(clientRegistration, "VIN" + "-" + String.format("%07d", numberRandom.nextInt(1000000)), LocalDateTime.now(), 0.0, false, AccountType.SAVINGS);
                } else {
                        accountNew = new Account(clientRegistration, "VIN" + "-" + String.format("%07d", numberRandom.nextInt(1000000)), LocalDateTime.now(), 0.0, false, AccountType.CHECKING);
                }

                clientRepository.save(clientRegistration);
                accountRepository.save(accountNew);
                return new ResponseEntity<>(HttpStatus.CREATED);

        }

        @GetMapping("/api/clients")
        public List<ClientDTO> getClients() {
              return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
        }

        @GetMapping("/api/clients/{id}")
        public ClientDTO getClient(@PathVariable Long id){
                return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
        }

        @GetMapping("/api/clients/current")
        public ClientDTO getClientDTO(Authentication authentication) {
                Client client = clientRepository.findByEmail(authentication.getName());
                return new ClientDTO(client);
         }
}


