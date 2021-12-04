package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class TransactionsController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PdfService pdfService;

    @Transactional
    @PostMapping("/api/clients/current/transactions")
    public ResponseEntity<Object> createNewTransaction(Authentication authentication, @RequestParam String amount, @RequestParam String description,
                                                         @RequestParam String originAccount, @RequestParam String destinationAccount) {
        Client clientAutenticado = clientRepository.findByEmail(authentication.getName());
        Account verifyOrigenAccount = accountRepository.findByNumber(originAccount);
        Account verifyDestinationAccount = accountRepository.findByNumber(destinationAccount);

        Set<Account> numberAccounts = clientAutenticado.getAccounts().stream().filter(numberAccount -> numberAccount.getNumber().contains(originAccount)).collect(Collectors.toSet());

        if (Double.parseDouble(amount) <= 0 || description.isEmpty() || originAccount.isEmpty() || destinationAccount.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN); //Verificar que los parámetros no estén vacíos
        }
        if(originAccount.equals(destinationAccount)){
            return new ResponseEntity<>("The accounts are the same", HttpStatus.FORBIDDEN); //Verificar que los números de cuenta no sean iguales
        }
        if (verifyOrigenAccount.getNumber() == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN); //Verificar que exista la cuenta de origen
        }
        if(numberAccounts.isEmpty()){
            return new ResponseEntity<>("Account is not authenticated client", HttpStatus.FORBIDDEN); //Verificar que la cuenta de origen pertenezca al cliente autenticado
        }
        if (verifyDestinationAccount.getNumber() == null) {
            return new ResponseEntity<>("Account does not exist", HttpStatus.FORBIDDEN); //Verificar que exista la cuenta de destino
        }
        if (verifyOrigenAccount.getBalance() < Double.parseDouble(amount)){
            return new ResponseEntity<>("Accounts do not have enough balance", HttpStatus.FORBIDDEN); //Verificar que la cuenta de origen tenga el monto disponible.
        }

        double newBalanceOrigen = verifyOrigenAccount.getBalance() - Double.parseDouble(amount);
        double newBalanceDestine = verifyDestinationAccount.getBalance() + Double.parseDouble(amount);

        verifyOrigenAccount.setBalance(newBalanceOrigen);
        verifyDestinationAccount.setBalance(newBalanceDestine);

        Transaction transactionDebit  =  new Transaction(verifyOrigenAccount, TransactionType.DEBIT, Double.parseDouble(amount), description, LocalDateTime.now(), verifyOrigenAccount.getBalance());
        transactionRepository.save(transactionDebit);

        Transaction transactionCredit  =  new Transaction(verifyDestinationAccount, TransactionType.CREDIT, Double.parseDouble(amount), description, LocalDateTime.now(), verifyDestinationAccount.getBalance());
        transactionRepository.save(transactionCredit);

       accountRepository.save(verifyOrigenAccount);
       accountRepository.save(verifyDestinationAccount);

       return new ResponseEntity<>(HttpStatus.CREATED);
    }//ResponseEntity

    @PostMapping("/api/transactions/pdf")
    public ResponseEntity<?> generatePDF(HttpServletResponse httpServletResponse, Authentication authentication,
                                         @RequestParam String numberAccount1, @RequestParam String numberAccount2,
                                         @RequestParam double amount, @RequestParam String description) throws IOException {

        httpServletResponse.setContentType("application/pdf");
        Client client1 = clientRepository.findByEmail(authentication.getName());
        Client client2 = accountRepository.findByNumber(numberAccount2).getClient();
        Account destinationAccount = accountRepository.findByNumber(numberAccount2);
        DateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss");
        String currentDatTime= dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attacment; filename=Transfer_a_"+destinationAccount.getNumber()+"_"+currentDatTime+".pdf";
        httpServletResponse.setHeader(headerKey,headerValue);

        this.pdfService.export(httpServletResponse, client1, client2, numberAccount1, numberAccount2, amount, description);
        return new ResponseEntity<>("Pdf ok ", HttpStatus.OK);
    }

}//transactionsController


