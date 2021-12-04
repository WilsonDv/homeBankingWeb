package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private boolean accountDelete = false;

    //Uno a muchos con transaction
    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Transaction> transactions= new HashSet<>();

    //uno a muchos con card
    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Card> cardPayments = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Account() {}

    public Account(Client client, String number, LocalDateTime date, double balance, boolean accountDelete, AccountType accountType ) {
        this.client = client;
        this.creationDate = date;
        this.number = number;
        this.balance = balance;
        this.accountDelete = accountDelete;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isAccountDelete() { return accountDelete;}
    public void setAccountDelete(boolean accountDelete) { this.accountDelete = accountDelete; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @JsonIgnore
    public Set<Card> getCardPayments() { return cardPayments;}
    public void setCardPayments(Set<Card> cardPayments) { this.cardPayments = cardPayments; }

    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }
}
