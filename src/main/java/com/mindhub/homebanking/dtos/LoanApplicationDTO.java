package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.Loan;


public class LoanApplicationDTO {
    private Long id;
    private double amount;
    private Integer payments;
    private String destinationAccount;
    private Loan loan;
    private String name;

    public LoanApplicationDTO() { }

    public LoanApplicationDTO(String name, double amount , Integer payments, String destinationAccount) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;

        this.destinationAccount = destinationAccount;
    }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name;}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }
    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
}
