package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.*;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			Random cardNumberRandom = new Random();
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("HolaMundo"));
			Client client2 = new Client("Wilson", "Martinez", "Will@tumail.ocm", passwordEncoder.encode("HolaMundo2"));
			Client client3 = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("HolaMundo3"));

			Account account1 = new Account(client1,"VIN-0000001" , LocalDateTime.now(), 5000.00, false, AccountType.SAVINGS);
			Account account2 = new Account(client1,"VIN" + "-" + String.format("%07d",cardNumberRandom.nextInt(1000000) + 1), LocalDateTime.now().plusDays(1), 7500.00, false, AccountType.CHECKING);
			Account account3 = new Account(client2,"VIN-0000002", LocalDateTime.now().plusDays(1), 7500.00, false, AccountType.CHECKING);

			Transaction transaction1 = new Transaction(account1, TransactionType.CREDIT, 2000.00, "Payment",LocalDateTime.now(), 8000);
			Transaction transaction2 = new Transaction(account1, TransactionType.DEBIT, 2000.00, "Salary", LocalDateTime.now(), 6000);
			Transaction transaction3 = new Transaction(account1, TransactionType.DEBIT, 500.00, "Payroll", LocalDateTime.now(), 5500);
			Transaction transaction4 = new Transaction(account1, TransactionType.DEBIT, 300.00, "Payroll", LocalDateTime.now(),  5200);
			Transaction transaction5 = new Transaction(account1, TransactionType.CREDIT, 2200.00, "Payment", LocalDateTime.now(),  7400);
			Transaction transaction6 = new Transaction(account1, TransactionType.DEBIT, 400.00, "Payroll", LocalDateTime.now(),  7000);
			Transaction transaction7 = new Transaction(account1, TransactionType.DEBIT, 2000.00, "Payment", LocalDateTime.now(),  5000);
			Transaction transaction8 = new Transaction(account2, TransactionType.DEBIT, 4000.00, "Payment", LocalDateTime.now(), 10000);
			Transaction transaction9 = new Transaction(account2, TransactionType.CREDIT, 2000.00, "Salary", LocalDateTime.now(),  12000);
			Transaction transaction10 = new Transaction(account2, TransactionType.DEBIT, 4500.00, "Payment", LocalDateTime.now(),  7500);


			Loan loan1 = new Loan( "Mortgage", 500000, List.of(12,24,36,48,60), 1.15);
			Loan loan2 = new Loan( "Personal", 100000, List.of(6,12,24), 1.20);
			Loan loan3 = new Loan( "Automotive", 300000, List.of(6,12,24,36), 1.30);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			Card card1 = new Card(client1, account1, CardType.DEBIT, CardColor.GOLD, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000)+1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false);
			Card card2 = new Card(client1, account2, CardType.CREDIT, CardColor.TITANIUM, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000)+1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false);
			Card card3 = new Card(client2, account3, CardType.CREDIT, CardColor.SILVER, String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1) +"-"+String.format("%04d", cardNumberRandom.nextInt(1000)+1), String.format("%03d", cardNumberRandom.nextInt(1000)+1), LocalDateTime.now().plusYears(5), LocalDateTime.now(), false);

			client1.addAccount(account1);
			client1.addAccount(account2);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			
		};
	}
}


