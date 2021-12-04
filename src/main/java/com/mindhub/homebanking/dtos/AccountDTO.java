package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean accountDelete = false;
    private AccountType accountType;

    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO() { }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.accountDelete = account.isAccountDelete();
        this.accountType = account.getAccountType();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Set<TransactionDTO> getTransactions() { return transactions; }
    public void setTransactions(Set<TransactionDTO> transactions) { this.transactions = transactions; }

    public boolean isAccountDelete() { return accountDelete; }
    public void setAccountDelete(boolean accountDelete) { this.accountDelete = accountDelete; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
