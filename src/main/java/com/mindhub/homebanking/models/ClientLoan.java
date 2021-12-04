package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class ClientLoan {

    private double amount;
    private Integer payments;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan() { }

    public ClientLoan(double amount,  Integer  payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount( double amount) {
        this.amount = amount;
    }

    public  Integer getPayments() {
        return payments;
    }
    public void setPayments( Integer payments) {
        this.payments = payments;
    }

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }
    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    @Override
    public String toString() {
        return "ClientLoan{" +
                "amount=" + amount +
                ", payments=" + payments +
                ", id=" + id +
                ", client=" + client +
                ", loan=" + loan +
                '}';
    }
}
